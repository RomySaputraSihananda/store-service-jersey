[![Twitter: romy](https://img.shields.io/twitter/follow/RomySihananda)](https://twitter.com/RomySihananda)

# store-service with Jersey

![](https://asset.kompas.com/crops/rfw-6z8NRJohYVC34YGa4XV69VA=/0x21:1080x741/750x500/data/photo/2023/02/13/63e9d84d1ead9.jpg)

# Dependency

- org.springdoc:springdoc-openapi-starter-webmvc-ui:**2.2.0**
- co.elastic.clients:elasticsearch-java:**8.10.4**
- com.fasterxml.jackson.core:jackson-databind:**2.15.2**
- org.projectlombok:lombok:**1.18.30**
- org.springframework.boot:spring-boot-starter-web:**undefined**
- org.projectlombok:lombok:**undefined**
- org.springframework.boot:spring-boot-starter-test:**undefined**
- org.springdoc:springdoc-openapi-starter-webmvc-ui:**2.1.0**
- com.sun.jersey:jersey-client:**1.19.4**
- com.fasterxml.jackson.core:jackson-databind:**2.15.2**

# EndPoints

The following is a list of available Request URIs:

- **GET** API for get all surat.
  - http://127.0.0.1:4444/api/v1/alquran
- **GET** API for get detail surat.
  - http://127.0.0.1:4444/api/v1/alquran/{nomorSurat}
- **GET** API for get tafsir surat.
  - http://127.0.0.1:4444/api/v1/alquran/tafsir/{nomorSurat}
- **GET** API for search by name surat.
  - http://127.0.0.1:4444/api/v1/alquran/search/nama/{namaSurat}
- **GET** API for search by tempat turun surat.
  - http://127.0.0.1:4444/api/v1/alquran/search/tempatTurun/{tempatTurun}

## Licence

All source code is licensed under the GNU General Public License v3. Please [see](https://www.gnu.org/licenses) the original document for more details.
