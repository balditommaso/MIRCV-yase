package it.unipi.dii.mircv.yase.indexing;

import it.unipi.dii.mircv.yase.structures.ArrayFile;
import it.unipi.dii.mircv.yase.structures.LexiconEntry;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Class used to handle partial lexicons during indexing.
 * Implements an ordered array on disk to store the lexicon's entries.
 * Each entry is of type {@link LexiconEntry} and has a fixed size, storing a term of maxStringLength length,
 * two offsets to retrieve the postings data of the term and an int for the document frequency of the
 * term.
 * This class extends the {@link ArrayFile} class which implements a disk based array
 */
public class PartialLexicon extends ArrayFile<String, LexiconEntry> {
    private MappedByteBuffer memoryMappedFile;
    private final static Logger logger = Logger.getLogger(PartialLexicon.class);

    /**
     * Constructor of the partial lexicon
     * @param path to the directory
     * @param fileName of the file
     * @param maxStringLength max number of char that string can have
     */
    public PartialLexicon(String path, String fileName, final int maxStringLength) {
        // invoke the constructor of ArrayFile
        super(path, fileName, maxStringLength);
        memoryMappedFile = null;
    }

    /**
     * Method to activate the <a href="https://www.geeksforgeeks.org/what-is-memory-mapped-file-in-java/">memory-mapping</a>
     * optimization
     */
    public void enableMemoryMap() {
        // enable the memory mapping, only during merging phase.
        try {
            memoryMappedFile = filePointer.getChannel()
                                .map(FileChannel.MapMode.READ_ONLY, 0, filePointer.length());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Method to read the {@link LexiconEntry} sequentially from the buffer.
     *
     * @return the entry.
     */
    public LexiconEntry readNextMMap() {
        return readEntry();
    }

    /**
     * Serializes a single {@link LexiconEntry} into a {@link MappedByteBuffer}.
     *
     * @param entry the entry to serialize
     * @param buffer the buffer used for memory-mapping
     */
    protected void writeEntry(final LexiconEntry entry, final MappedByteBuffer buffer) {
        //format the term by adding spaces if shorter than max length
        String fixedSizeTerm = String.format("%1$"+maxStringLength+ "s", entry.getTerm());

        //write key-value pair sequentially
        buffer.put(fixedSizeTerm.getBytes(StandardCharsets.US_ASCII));
        buffer.putLong(entry.getOffsetDocid());
    }

    /**
     * Returns the fixed size of each LexiconEntry, considering the specified max string length.
     *
     * @return the size.
     */
    @Override
    protected int getEntrySize(int maxStringLength) {
        return maxStringLength + Long.BYTES;
    }

    /**
     * Reads a single LexiconEntry already at the correct offset.
     *
     * @return entry
     */
    @Override
    protected LexiconEntry readEntry() {
        // read the term
        byte[] buf = new byte[maxStringLength];
        memoryMappedFile.get(buf);
        String term = new String(buf, StandardCharsets.US_ASCII);
        // read the offset
        long docidOffset = memoryMappedFile.getLong();

        return new LexiconEntry(term, docidOffset);
    }

    /**
     * Retrieve the entry by key using the binary search.
     *
     * @param key the key of the entry to retrieve
     * @return the searched {@link LexiconEntry}
     */
    @Override
    protected LexiconEntry retrieveEntry(final String key) {
        return binarySearch(key);
    }
}

