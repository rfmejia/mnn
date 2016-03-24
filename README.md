# Metabolomics Node Network

**mnn** is a network for reactive metabolomics data storage and processing, with a hypermedia API access and simple HTML web application for searching and browsing data.

**Technologies:**

- Written in Scala
- Primarily produces HAL-JSON responses [specification](http://stateless.co/hal_specification.html) with some application-specific additions
- Asynchronous request handling via [spray.io](http://spray.io)
- Compatible with multiple database backends via [Slick 3](http://slick.typesafe.com/)
- Responsive HTML5 web application built with [Polymer](http://polymer-project.org)

## Modules

- [common](common/README.md): Contains core domain types and algebra to work with mnn.

See each subproject for installation instructions.
