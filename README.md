# SpringBoot WITH ELASTICSEARCH & KIBANA TUTORIAL

<img src="https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg" width="48">
<img src="https://images.contentstack.io/v3/assets/bltefdd0b53724fa2ce/blt280217a63b82a734/5bbdaacf63ed239936a7dd56/elastic-logo.svg" width="48">

### Prerequisites
 - JDK 11
 - Docker version 20.10.10
 

### Useful links  
 - https://www.elastic.co/guide/en/kibana/current/docker.html 
 - https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html
 - https://www.youtube.com/watch?v=gS_nHTWZEJ8&t=2s

Alternative: You can download elasticsearch and kibana directly in your local without the need of Docker but since we need to also have an elasticsearch environment for testing purposes we will be using Docker. 

Create user defined network (useful for connecting to other services attached to the same network (e.g. Kibana)):
```sh
$ docker network create elastic
```

Get and run elasticsearch and change directly the cluster name and the node name:
```shell
$ docker run -d --name es01-test --net elastic -p 127.0.0.1:9200:9200 -p 127.0.0.1:9300:9300  -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" -e "discovery.type=single-node" -e "cluster.name=elastic-tutorial_cluster" -e "node.name=elastic-tutorial-node" docker.elastic.co/elasticsearch/elasticsearch:7.16.2
```

Run Kibana:
```shell
$ docker run -d --name kibana-elastic-tut --net elastic -p 127.0.0.1:5601:5601 -e "ELASTICSEARCH_URL=http://es01-test:9200" -e "ELASTICSEARCH_HOSTS=http://es01-test:9200" docker.elastic.co/kibana/kibana:7.16.2
```

Hit the http://localhost:9200/ to check if ur container is up and running. Make sure that the node name is elastic-tutorial-node and the cluster name is elastic-tutorial_cluster. While using Elasticsearch it is recommended to have ur own appropriate cluster name and an appropriate node name. 

Hit the http://localhost:5601/  to check if Kibana is upp and running. Access the management console where we can write our own queries.
You can do that in ui by pressing the 
```
  -> Tab icon 
  -> Management 
  -> Dev Tools or by hitting the “http://localhost:5601/app/dev_tools#/console”. 
```
Here we can perform different CRUD operations: 

Get the nodes stats
```
GET _nodes/stats
```

Create an index
```
PUT favorite_candy
```

POST - Create a document inside an index using POST. By using POST it will autogenerate an unique id for this document.
```
POST favorite_candy/_doc
{
  _"first_name": "Lisa",
  "candy": "Sour Skittles"_
}
```

PUT - We use PUT when we want to assign a specific ID to the document. An useful scenario would is when we are indexing data with the natural identifier. For example lets say we're indexing patient data where each patient has a unique id. So in this case it would be easier to work with an document if it has the same id as a patient id rather than an auto-genereated id.
```
PUT favorite_candy/_doc/1
{
  "first_name":"John",
  "candy":"Startbust"
}
```
What would happen if we try to index a document with an id that already exists. By doing this we can see in the response that the version now have been change to 2 i.e how many times the document has been created, updated or deleted.
```
PUT favorite_candy/_doc/1
{
  "first_name":"Sally",
  "candy":"Snickers"
}
```
```
PUT favorite_candy/_doc/2
{
  "first_name":"Rachel",
  "candy":"Rolos"
}
```

```
PUT favorite_candy/_doc/3
{
  "first_name":"Tom",
  "candy":"Sweet Tarts"
}
```

#### READ - Now that we have indexed some documents i want to send a request to see the content of the document that has been indexed.
```
GET favorite_candy/_doc/1
```

####_create Endpoint
When you index a document using an id that already exists, the existing document is overwritten by the new document. If you do not want a existing document to be overwritten, you can use the _create endpoint.
#### With the _create Endpoint, no indexing will occur, and you will get a 409 error message
```
PUT favorite_candy/_create/1
{
  "first_name":"Finn",
  "candy":"Jolly Ranchers"
}
```
#### UPDATE
When updating a document make sure to have “doc” as a context. This basically tells Elasticsearch to only update a document with the field specified. 

Syntax:
```
POST Name-of-the-Index/_update/id-of-the-document-you-want-to-update
{
  "doc": {
    "field1": "value",
    "field2": "value",
  }
}
```

Example:
```
POST favorite_candy/_update/1
{
  "doc": {
    "candy": "M&M's"
  }
} 
```
Delete a document

Syntax:
```
DELETE Name-of-the-Index/_doc/id-of-the-document-you-want-to-delete
```
Example:
```
DELETE favorite_candy/_doc/1
```
#### Precision and recall
Precision and recall determine which documents are included in the search results but precision and recall does not determine which of these return documents are more relevant than the other ( This is determined by ranking).

#### _Precision_
I want all the retrieved results to be a perfect match to the query, even if it means returning less documents. #Recall
I want to retrieve more results even if documents may not be a perfect match to the query.
#### _Ranking_
Ranking refers to ordering of the results (from most relevant results at the top, to least relevant at the bottom). Ranking is determined by a scoring algorithm, so each result is given a score and ones with the highest score are displayed at the top whereas ones with the lowest score are displayed at the bottom.
#### _Score_
The score is a value that represents how relevant a document is to that specific query. A score is computed for each document that is a hit.
There are multiple factors that are used to compute a document store:
- Term Frequency (TF) (“Determines how many times each search term appears in a document”)
- Inverse Document Frequency (IDF) (“Diminishes the weight of terms that occur very frequently in the document set and increases the weight of terms that occur rarely”)

### Upload data from a file
We are going to upload some data in elasticsearch by using a document. https://www.kaggle.com/rmisra/news-category-dataset/version/2 Download the json file, unzip it then go to:
```
 -> Kibana UI
 -> Machine Learning 
 -> Data Vizualiser 
 -> File (drag and drop the downloaded file).
 -> Click Import
 -> Give an index name (news_headlines ) and select import again.
```

If we want to delete all of the documents that we just stored in elastic search, we can do that by deleting its index for example:
```
DELETE news_headlines
```
Getting search results from an index
```
GET news_headlines/_search
```

When we hit  "GET news_headlines/_search" elasticsearch return 10 items by default, and it tells us that the total hits is: 10000.

**By default, elasticsearch limits the total count to 10.000 and this is done to improve the response speed on large data sets.**

To see if 10.000 is the exact total number of hits/data we have, we can check the “relation” field that we get from the response of “GET news_headlines/_search”.

If the relation value is **“eq”**  that means the value is equal to the total number of hits. If the relation value is **“gte”** that means that the number of hits are equal to or greater than 10.000.
We can implicitly ask elasticsearch for the total numbers of hits by:
```
GET news_headlines/_search
{
  "track_total_hits": true
} 
```

-There are 2 main ways to search in elasticsearch and these are **queries** and **aggregations**.
### Queries 
Queries tell elastic search to retrieve documents that match/meet the criteria:
```
GET news_headlines/_search
{
  "query": {
    "range": {
      "date": {
        "gte": "2015-06-20",
        "lte": "2015-09-22"
      }
    }
  }
}
```
### Agregations
An aggregation summarises your data as metrics, statistics and other analytics i.e when we are not interested to ask elasticsearch for a specific criteria, but instead we are interested of what kind of news categories are there.


To get this information we need to **analyse** the data and get the summary of categories that exists on our data and this type of search is known as aggregation. 

Below we ask elasticsearch to analyse the news and get 100 different categories. 
We name this aggregation by_category.
```
GET news_headlines/_search
{
  "aggs": {
    "by_category": {
      "terms": {
        "field": "category",
        "size":100
      }
    }
  }
}
```
_RESPONSE_: Other than seeing the categories, elasticsearch will also count for us how many document we have for that specific category in a matter of seconds.
```
"buckets" : [
  {
    "key" : "POLITICS",
    "doc_count" : 32739
  },
  {
    "key" : "WELLNESS",
    "doc_count" : 17827
  },
   ...more results
];
```

#### Combination of query and aggregation request.
What if we want to identify an ENTERTAINMENT category. This is going to get done by a combination of query and aggregation request.

First we are going to pull all the documents from the entertainment (so we got to query the data first) then we have to analyse query data and give the summary of the most significant topics in the entertainment category i.e get
```
GET news_headlines/_search
{
  "query": {
    "match": {
      "category": "ENTERTAINMENT"
    }
  },
  "aggs": {
    "popular_in_entertainment": {
      "significant_text": {
        "field": "headline"
      }
    }
  }
}
```
_RESPONSE_:  We are basically analysing the ENTERTAINMENT news and checking which term/significant_text/key is mention mostly (in how many documents has been used in the headline) etc…
```
"buckets" : [
  {
    "key" : "trailer",
    "doc_count" : 387,
    "score" : 0.21929887729058337,
    "bg_count" : 479
  },
  {
    "key" : "movie",
    "doc_count" : 419,
    "score" : 0.16112359079830124,
    "bg_count" : 730
  },
  …more results
]
```                

#### DoingMoreCombination
What if we want to count how many times a specific term for instance “Kardashian” has come up in the news in a specific category.
```
GET news_headlines/_search
{
  "aggs": {
    "test_something": {
      "terms": { "field": "category" }
    }
  },
  "query": {
    "bool": {
      "must": [
        {
          "match": {
             "headline": "Kardashian"
          }
        },
        {
          "match": {
            "category": "ENTERTAINMENT"
          }
        }
      ]
    }
  }
}
```
 _RESPONSE_:
```
{
  …10 documents that matches the criteria
},
{
  "aggregations" : {
    "test_something" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "ENTERTAINMENT",
          "doc_count" : 332
        }
      ]
    }
  }
}
```

### Precision and recall
```
GET news_headlines/_search
{
  "query": {
    "match": {
      "headline": "Khloe Kardashian Kendell Jenner"
    }
  }
}
```
When running the above query we can notice in the response that we are not getting perfect matches.

Some documents headlines contains only one of the search term. Soo, notice that our search query contains 4 search terms: “_Khloe_”, “_Kardashain_”, “_Kendall_”, “_Jenner_” and by default the “match” query uses an “**OR**” logic.

So with the above query a document is considered a “HIT” if it contains even one of these search terms in the headline.

**As we can see that this query is better for improving _Recall_ and not the _Precision_.**

But if we want to increase _Precision_ instead we can add an “AND” operator to the request:
```
GET news_headlines/_search
{
  "query": {
    "match": {
      "headline": {
        "query": "Khloe Kardashian Kendell Jenner",
        "operator": "and"
      }
    }
  }
}
```
What the above query request does is only pulls the documents that contain all 4 of the search term in the headline.

Soo, we can see with the above examples that we can use _Recall_, and we can get multiple result as long as the field where we’re searching has at least 1 search term.

But whenever we want that the field where we are searching for contains all the search terms we pass we can use _Precision_.

There is also a way to land somewhere in between by using “minimum_should_match” parameter for instance 3.
By doing we are saying that at least 3 search terms must be included in the headlines:
```
GET news_headlines/_search
{
  "query": {
    "match": {
      "headline": {
        "query": "Khloe Kardashian Kendall Jenner",
        "minimum_should_match": 3
      }
    }
  }  
}
```

Soo, the **minimum_should_match** parameters is a great way to narrow the net without being too strict, soo this is a good parameter to use when we are fine-tuning **Precision** and **Recall**.

### Spring Boot
In this tutorial we will be using the Elasticsearch rest low level client.   \
There is also an abstraction library from the elasticsearch team: <a href="https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/introduction.html"> Elasticsearch Java API</a> \
Another abstraction library from the spring boot community: <a href="https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#reference"> Spring Data Elasticsearch</a>

Add to gradle:
```
  implementation 'org.elasticsearch.client:elasticsearch-rest-client:7.15.2' 
```

<a href="https://www.elastic.co/guide/en/elasticsearch/reference/7.16/security-minimal-setup.html">Enable security and update password for elastic-stack</a> 

Once you complete the step for enabling security and updating the password we will see something like this in our terminal:
```shell
Changed password for user [apm_system]
Changed password for user [kibana_system]
Changed password for user [kibana]
Changed password for user [logstash_system]
Changed password for user [beats_system]
Changed password for user [remote_monitoring_user]
Changed password for user [elastic]
```
<a href="https://www.elastic.co/guide/en/elasticsearch/reference/7.16/security-minimal-setup.html#add-built-in-users  ">Configure Kibana to connect to Elasticsearch with a password</a>\

We can secure data by adding Spaces, Roles and creating User  to Kibana:  https://www.elastic.co/guide/en/kibana/7.16/tutorial-secure-access-to-kibana.html  

<a href="https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_basic_authentication.html"> Add configuration for elastic authentication in java</a>

Build application:
```shell
$ ./gradlew clean build
```

Run application in a local environment.\
OBS! The **dev** profile is the default one. See build.gradle
```shell
$ ./gradlew bootRun
```

Run application in a production environment:\
OBS! The passed parameters are the local params. Pass **prod** environment when you deploy your app to production.
```shell
$ ./gradlew bootRun -Pprofile=prod -Pelastic-user=elastic -Pelastic-password=fazlizekiqi -Pelastic-host=localhost -Pelastic-ports=9200,9201    
```
