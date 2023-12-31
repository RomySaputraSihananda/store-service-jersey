[![Twitter: romy](https://img.shields.io/twitter/follow/RomySihananda)](https://twitter.com/RomySihananda)

# store-service with Jersey

![](https://raw.githubusercontent.com/RomySaputraSihananda/RomySaputraSihananda/main/images/fr2.jpg)

Store Services Rest API with Sping Boot v3.1.5</br>uses Elasticsearch as Storage Database, Jersey Client for send request to ElasticSearch</br> and also Swagger UI for Endpoint API documentation.

# Dependency

- org.springframework.boot:spring-boot-starter-web:**3.1.5**
- org.projectlombok:lombok:**1.18.30**
- org.springdoc:springdoc-openapi-starter-webmvc-ui:**2.1.0**
- com.sun.jersey:jersey-client:**1.19.4**
- com.fasterxml.jackson.core:jackson-databind:**2.15.2**

# EndPoints

The following is a list of available Request URIs:

- **GET** API for get products with pagination.
  - http://127.0.0.1:4444/api/v1/product
- **GET** API for get product by id.
  - http://127.0.0.1:4444/api/v1/product/{id}
- **POST** API for create new product.
  - http://127.0.0.1:4444/api/v1/product
- **PUT** API for update product.
  - http://127.0.0.1:4444/api/v1/product/{id}
- **DEL** API for delete product.
  - http://127.0.0.1:4444/api/v1/product/{id}
- **GET** API for get product by name.
  - http://127.0.0.1:4444/api/v1/product/search

## Licence

All source code is licensed under the GNU General Public License v3. Please [see](https://www.gnu.org/licenses) the original document for more details.
