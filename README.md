# YASE
YASE is a search engine capable of providing search results for the **MS Marco document collection**, a dataset available on GitHub.
The application is composed of the following modules:
- Indexer: creates the data structures, such as the inverted index, lexicon and document index;
- Query processor: supports conjunctive and disjunctive free text queries, and returns the search results. It implements both TF-IDF and BM25 scoring functions.

Furthermore, YASE employs dynamic pruning techniques such as MaxScore in order to boost the performances of disjunctive queries. The search engine also adopts simple compression techniques to reduce the size of the inverted index, in order to be efficient in terms of memory usage while maintaining fast search response.  

The user can take advantage of YASE functionalities through a user-friendly CLI.

## Set up

Once imported the project you can retrieve the * *jar* * lunching the following command from inside the **SearchEngine** folder:
```
mvn assembly:assembly -DdescriptorId=jar-with-dependencies install 
```
Now you should have * *yase.jar* * in the **YASE** root folder (the one with the LICENSE in it) and you can run it with one of the following options:
```
usage: java -jar yase.jar [options]
  -i,--index      Create the index by processing the collection at the following path:
                  'SearchEngine/src/main/resources/collection.tar.gz' (MSMARCO collection)
  -s,--search     Start using the search engine (Index is needed).
  -t,--test       Creates a results file ready to be used to evaluate the effectiveness
                  of the search engine with trec_eval (Index is needed).
```
**NOTE**: remember to move the MS Marco collection in the following folder:
* *YASE/SearchEngine/src/main/resources/collection.tar.gz* 
## Contributors

- Tommaso Baldi [@balditommaso](https://github.com/balditommaso)
- Federico Minniti [@federicominniti](https://github.com/federicominniti)
- Matteo Del Seppia [@matteodelseppia](https://github.com/matteodelseppia)

