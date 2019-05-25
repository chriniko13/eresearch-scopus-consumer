# Eresearch Elsevier Scopus Consumer Service #

### Description
The purpose of this service is to consume the info which is provided from the
following elsevier api: http://api.elsevier.com/documentation/SCOPUSSearchAPI.wadl

The search view (result from the consuming of the service we get) 
is the following: http://api.elsevier.com/documentation/search/SCOPUSSearchViews.htm
we get the COMPLETE view.

See also:
*  http://api.elsevier.com/documentation/search/SCOPUSSearchTips.htm
*  http://api.elsevier.com/content/search/fields/scopus

The search query we use are the following:
* query=au-id(23007591800) (in code this is what we use:"AU-ID(" + authorIdentifierNumber + ")&view=COMPLETE")

au-id stands for Author Identifier Number.


### External Dependencies needed in order to run service

* Academic VPN in order to fetch results from Elsevier API (https://dev.elsevier.com/documentation/ScienceDirectSearchAPI.wadl#d1e166)

* ActiveMQ
    * Execute: `docker-compose up`
    * Execute: `docker-compose down`
    
    
### Integration Tests (run docker-compose first)

* Execute: `mvn clean verify`    


### Create Docker Image
* Execute: `mvn clean install -DskipITs=true`
* Execute: `docker build -t chriniko/eresearch-scopus-consumer:1.0 .` in order to build docker image.

* Fast: `mvn clean install -DskipITs=true && docker build -t chriniko/eresearch-scopus-consumer:1.0 .`


### How to run service (not dockerized)
* Execute: `docker-compose up`

* Two options:
    * Execute: 
        * `mvn clean install -DskipITs=true`
        * `java -jar -Dspring.profiles.active=dev target/eresearch-elsevier-scopus-consumer-1.0-boot.jar`
                
    * Execute:
        * `mvn spring-boot:run -Dspring.profiles.active=dev`

* (Optional) When you finish: `docker-compose down`


### How to run service (dockerized)
* Uncomment the section in `docker-compose.yml` file for service: `eresearch-scopus-consumer:`

* Execute: `mvn clean install -DskipITs=true`

* Execute: `docker-compose build`

* Execute: `docker-compose up`

* (Optional) When you finish: `docker-compose down`


### Example Request

```json

{
	"au-id":"23007591800"
}

```



### Example Response

```json
{
    "operation-result": true,
    "process-finished-date": "2019-04-16T22:11:59.629Z",
    "requested-elsevier-scopus-consumer-dto": {
        "au-id": "23007591800"
    },
    "fetched-results-size": 2,
    "fetched-results": [
        {
            "search-results": {
                "opensearch:totalResults": "42",
                "opensearch:startIndex": "0",
                "opensearch:itemsPerPage": "25",
                "opensearch:Query": {
                    "@role": "request",
                    "@searchTerms": "AU-ID(23007591800)",
                    "@startPage": "0"
                },
                "link": [
                    {
                        "@_fa": "true",
                        "@href": "https://api.elsevier.com/content/search/scopus?start=0&count=25&query=AU-ID%2823007591800%29&view=COMPLETE",
                        "@ref": "self",
                        "@type": "application/json"
                    },
                    {
                        "@_fa": "true",
                        "@href": "https://api.elsevier.com/content/search/scopus?start=0&count=25&query=AU-ID%2823007591800%29&view=COMPLETE",
                        "@ref": "first",
                        "@type": "application/json"
                    },
                    {
                        "@_fa": "true",
                        "@href": "https://api.elsevier.com/content/search/scopus?start=25&count=25&query=AU-ID%2823007591800%29&view=COMPLETE",
                        "@ref": "next",
                        "@type": "application/json"
                    },
                    {
                        "@_fa": "true",
                        "@href": "https://api.elsevier.com/content/search/scopus?start=17&count=25&query=AU-ID%2823007591800%29&view=COMPLETE",
                        "@ref": "last",
                        "@type": "application/json"
                    }
                ],
                "entry": [
                    {
                        "@force-array": null,
                        "error": null,
                        "@_fa": "true",
                        "link": [
                            {
                                "@_fa": "true",
                                "@href": "https://api.elsevier.com/content/abstract/scopus_id/85045740431",
                                "@ref": "self",
                                "@type": null
                            },
                            {
                                "@_fa": "true",
                                "@href": "https://api.elsevier.com/content/abstract/scopus_id/85045740431?field=author,affiliation",
                                "@ref": "author-affiliation",
                                "@type": null
                            },
                            {
                                "@_fa": "true",
                                "@href": "https://www.scopus.com/inward/record.uri?partnerID=HzOxMe3b&scp=85045740431&origin=inward",
                                "@ref": "scopus",
                                "@type": null
                            },
                            {
                                "@_fa": "true",
                                "@href": "https://www.scopus.com/inward/citedby.uri?partnerID=HzOxMe3b&scp=85045740431&origin=inward",
                                "@ref": "scopus-citedby",
                                "@type": null
                            }
                        ],
                        "prism:url": "https://api.elsevier.com/content/abstract/scopus_id/85045740431",
                        "dc:identifier": "SCOPUS_ID:85045740431",
                        "eid": "2-s2.0-85045740431",
                        "dc:title": "Evaluation of the HadGEM3-A simulations in view of detection and attribution of human influence on extreme events in Europe",
                        "prism:aggregationType": "Journal",
                        "citedby-count": "5",
                        "prism:publicationName": "Climate Dynamics",
                        "prism:isbn": null,
                        "prism:issn": "09307575",
                        "prism:eIssn": "14320894",
                        "prism:volume": "52",
                        "prism:issueIdentifier": "1-2",
                        "prism:pageRange": "1187-1210",
                        "prism:coverDate": "2019-01-24",
                        "prism:coverDisplayDate": "24 January 2019",
                        "prism:doi": "10.1007/s00382-018-4183-6",
                        "pii": null,
                        "pubmed-id": null,
                        "orcid": null,
                        "dc:creator": "Vautard R.",
                        "affiliation": [
                            {
                                "@_fa": "true",
                                "affilname": "Universite Paris-Saclay",
                                "affiliation-city": "Saint-Aubin",
                                "affiliation-country": "France",
                                "afid": "60106017",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60106017",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "Met Office",
                                "affiliation-city": "Exeter",
                                "affiliation-country": "United Kingdom",
                                "afid": "60018258",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60018258",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "Centro Nacional de Supercomputación",
                                "affiliation-city": "Barcelona",
                                "affiliation-country": "Spain",
                                "afid": "60097745",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60097745",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "Danmarks Meteorologiske Institut",
                                "affiliation-city": "Copenhagen",
                                "affiliation-country": "Denmark",
                                "afid": "60014500",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60014500",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "University of Edinburgh",
                                "affiliation-city": "Edinburgh",
                                "affiliation-country": "United Kingdom",
                                "afid": "60027272",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60027272",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "Royal Netherlands Meteorological Institute",
                                "affiliation-city": "De Bilt",
                                "affiliation-country": "Netherlands",
                                "afid": "60000203",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60000203",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "ETH Zürich",
                                "affiliation-city": "Zurich ZH,",
                                "affiliation-country": "Switzerland",
                                "afid": "60025858",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60025858",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "Helmholtz-Zentrum Geesthacht - Zentrum für Material- und Küstenforschung GmbH",
                                "affiliation-city": "Geesthacht",
                                "affiliation-country": "Germany",
                                "afid": "60025288",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60025288",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "University of Reading",
                                "affiliation-city": "Reading",
                                "affiliation-country": "United Kingdom",
                                "afid": "60012197",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60012197",
                                "name-variant": null
                            },
                            {
                                "@_fa": "true",
                                "affilname": "University of Leeds",
                                "affiliation-city": "Leeds",
                                "affiliation-country": "United Kingdom",
                                "afid": "60012070",
                                "affiliation-url": "https://api.elsevier.com/content/affiliation/affiliation_id/60012070",
                                "name-variant": null
                            }
                        ],
                        "author": [
                            {
                                "@_fa": "true",
                                "@seq": "1",
                                "author-url": "https://api.elsevier.com/content/author/author_id/56253852700",
                                "authid": "56253852700",
                                "orcid": null,
                                "authname": "Vautard R.",
                                "given-name": "Robert",
                                "surname": "Vautard",
                                "initials": "R.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60106017"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "2",
                                "author-url": "https://api.elsevier.com/content/author/author_id/23007591800",
                                "authid": "23007591800",
                                "orcid": null,
                                "authname": "Christidis N.",
                                "given-name": "Nikolaos",
                                "surname": "Christidis",
                                "initials": "N.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60018258"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "3",
                                "author-url": "https://api.elsevier.com/content/author/author_id/56487514000",
                                "authid": "56487514000",
                                "orcid": null,
                                "authname": "Ciavarella A.",
                                "given-name": "Andrew",
                                "surname": "Ciavarella",
                                "initials": "A.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60018258"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "4",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55833427300",
                                "authid": "55833427300",
                                "orcid": null,
                                "authname": "Alvarez-Castro C.",
                                "given-name": "Carmen",
                                "surname": "Alvarez-Castro",
                                "initials": "C.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60106017"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "5",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55340272500",
                                "authid": "55340272500",
                                "orcid": null,
                                "authname": "Bellprat O.",
                                "given-name": "Omar",
                                "surname": "Bellprat",
                                "initials": "O.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60097745"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "6",
                                "author-url": "https://api.elsevier.com/content/author/author_id/7005170580",
                                "authid": "7005170580",
                                "orcid": null,
                                "authname": "Christiansen B.",
                                "given-name": "Bo",
                                "surname": "Christiansen",
                                "initials": "B.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60014500"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "7",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55763887900",
                                "authid": "55763887900",
                                "orcid": null,
                                "authname": "Colfescu I.",
                                "given-name": "Ioana",
                                "surname": "Colfescu",
                                "initials": "I.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60027272"
                                    },
                                    {
                                        "@_fa": "true",
                                        "$": "60012070"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "7",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55763887900",
                                "authid": "55763887900",
                                "orcid": null,
                                "authname": "Colfescu I.",
                                "given-name": "Ioana",
                                "surname": "Colfescu",
                                "initials": "I.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60027272"
                                    },
                                    {
                                        "@_fa": "true",
                                        "$": "60012070"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "8",
                                "author-url": "https://api.elsevier.com/content/author/author_id/12041435300",
                                "authid": "12041435300",
                                "orcid": null,
                                "authname": "Cowan T.",
                                "given-name": "Tim",
                                "surname": "Cowan",
                                "initials": "T.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60027272"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "9",
                                "author-url": "https://api.elsevier.com/content/author/author_id/6603432436",
                                "authid": "6603432436",
                                "orcid": null,
                                "authname": "Doblas-Reyes F.",
                                "given-name": "Francisco",
                                "surname": "Doblas-Reyes",
                                "initials": "F.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60097745"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "10",
                                "author-url": "https://api.elsevier.com/content/author/author_id/57202619821",
                                "authid": "57202619821",
                                "orcid": null,
                                "authname": "Eden J.",
                                "given-name": "Jonathan",
                                "surname": "Eden",
                                "initials": "J.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60000203"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "11",
                                "author-url": "https://api.elsevier.com/content/author/author_id/56684259500",
                                "authid": "56684259500",
                                "orcid": null,
                                "authname": "Hauser M.",
                                "given-name": "Mathias",
                                "surname": "Hauser",
                                "initials": "M.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60025858"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "12",
                                "author-url": "https://api.elsevier.com/content/author/author_id/57203054070",
                                "authid": "57203054070",
                                "orcid": null,
                                "authname": "Hegerl G.",
                                "given-name": "Gabriele",
                                "surname": "Hegerl",
                                "initials": "G.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60027272"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "13",
                                "author-url": "https://api.elsevier.com/content/author/author_id/57196442844",
                                "authid": "57196442844",
                                "orcid": null,
                                "authname": "Hempelmann N.",
                                "given-name": "Nils",
                                "surname": "Hempelmann",
                                "initials": "N.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60106017"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "14",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55800808000",
                                "authid": "55800808000",
                                "orcid": null,
                                "authname": "Klehmet K.",
                                "given-name": "Katharina",
                                "surname": "Klehmet",
                                "initials": "K.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60025288"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "15",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55670287400",
                                "authid": "55670287400",
                                "orcid": null,
                                "authname": "Lott F.",
                                "given-name": "Fraser",
                                "surname": "Lott",
                                "initials": "F.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60018258"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "16",
                                "author-url": "https://api.elsevier.com/content/author/author_id/57204223005",
                                "authid": "57204223005",
                                "orcid": null,
                                "authname": "Nangini C.",
                                "given-name": "Cathy",
                                "surname": "Nangini",
                                "initials": "C.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60106017"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "17",
                                "author-url": "https://api.elsevier.com/content/author/author_id/55346607800",
                                "authid": "55346607800",
                                "orcid": null,
                                "authname": "Orth R.",
                                "given-name": "René",
                                "surname": "Orth",
                                "initials": "R.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60025858"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "18",
                                "author-url": "https://api.elsevier.com/content/author/author_id/36157178900",
                                "authid": "36157178900",
                                "orcid": null,
                                "authname": "Radanovics S.",
                                "given-name": "Sabine",
                                "surname": "Radanovics",
                                "initials": "S.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60106017"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "19",
                                "author-url": "https://api.elsevier.com/content/author/author_id/7003499456",
                                "authid": "7003499456",
                                "orcid": null,
                                "authname": "Seneviratne S.",
                                "given-name": "Sonia I.",
                                "surname": "Seneviratne",
                                "initials": "S.I.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60025858"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "20",
                                "author-url": "https://api.elsevier.com/content/author/author_id/6701843355",
                                "authid": "6701843355",
                                "orcid": null,
                                "authname": "van Oldenborgh G.",
                                "given-name": "Geert Jan",
                                "surname": "van Oldenborgh",
                                "initials": "G.J.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60000203"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "21",
                                "author-url": "https://api.elsevier.com/content/author/author_id/22836973600",
                                "authid": "22836973600",
                                "orcid": null,
                                "authname": "Stott P.",
                                "given-name": "Peter",
                                "surname": "Stott",
                                "initials": "P.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60018258"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "22",
                                "author-url": "https://api.elsevier.com/content/author/author_id/7006280684",
                                "authid": "7006280684",
                                "orcid": null,
                                "authname": "Tett S.",
                                "given-name": "Simon",
                                "surname": "Tett",
                                "initials": "S.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60027272"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "23",
                                "author-url": "https://api.elsevier.com/content/author/author_id/53880473700",
                                "authid": "53880473700",
                                "orcid": null,
                                "authname": "Wilcox L.",
                                "given-name": "Laura",
                                "surname": "Wilcox",
                                "initials": "L.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60012197"
                                    }
                                ]
                            },
                            {
                                "@_fa": "true",
                                "@seq": "24",
                                "author-url": "https://api.elsevier.com/content/author/author_id/57203260074",
                                "authid": "57203260074",
                                "orcid": null,
                                "authname": "Yiou P.",
                                "given-name": "Pascal",
                                "surname": "Yiou",
                                "initials": "P.",
                                "afid": [
                                    {
                                        "@_fa": "true",
                                        "$": "60106017"
                                    }
                                ]
                            }
                        ],
                        "author-count": {
                            "@limit": "100",
                            "$": "25"
                        },
                        "dc:description": "© 2018, Springer-Verlag GmbH Germany, part of Springer Nature. A detailed analysis is carried out to assess the HadGEM3-A global atmospheric model skill in simulating extreme temperatures, precipitation and storm surges in Europe in the view of their attribution to human influence. The analysis is performed based on an ensemble of 15 atmospheric simulations forced with observed sea surface temperature of the 54 year period 1960–2013. These simulations, together with dual simulations without human influence in the forcing, are intended to be used in weather and climate event attribution. The analysis investigates the main processes leading to extreme events, including atmospheric circulation patterns, their links with temperature extremes, land–atmosphere and troposphere-stratosphere interactions. It also compares observed and simulated variability, trends and generalized extreme value theory parameters for temperature and precipitation. One of the most striking findings is the ability of the model to capture North-Atlantic atmospheric weather regimes as obtained from a cluster analysis of sea level pressure fields. The model also reproduces the main observed weather patterns responsible for temperature and precipitation extreme events. However, biases are found in many physical processes. Slightly excessive drying may be the cause of an overestimated summer interannual variability and too intense heat waves, especially in central/northern Europe. However, this does not seem to hinder proper simulation of summer temperature trends. Cold extremes appear well simulated, as well as the underlying blocking frequency and stratosphere-troposphere interactions. Extreme precipitation amounts are overestimated and too variable. The atmospheric conditions leading to storm surges were also examined in the Baltics region. There, simulated weather conditions appear not to be leading to strong enough storm surges, but winds were found in very good agreement with reanalyses. The performance in reproducing atmospheric weather patterns indicates that biases mainly originate from local and regional physical processes. This makes local bias adjustment meaningful for climate change attribution.",
                        "authkeywords": null,
                        "article-number": null,
                        "subtype": "ar",
                        "subtypeDescription": "Article",
                        "source-id": "12172",
                        "fund-acr": "SPACE",
                        "fund-no": "607085",
                        "fund-sponsor": "FP7 Space",
                        "message": null,
                        "openaccess": "0",
                        "openaccessFlag": "false"
                    },
                    
                    ...

```