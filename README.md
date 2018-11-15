# SQL Adapter

[![GitLicense](https://gitlicense.com/badge/sean0x42/SQLAdapter)](https://gitlicense.com/license/sean0x42/SQLAdapter)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/sean0x42/SQLAdapter.svg)](https://github.com/sean0x42/SQLAdapter)
[![GitHub issues](https://img.shields.io/github/issues/sean0x42/SQLAdapter.svg)](https://github.com/sean0x42/SQLAdapter/issues/)

A library for adapting Java objects into SQL by
[Sean Bailey](https://www.seanbailey.io). Inspired by ActiveRecord. This library
was originally built very quickly for a university assignment.


## Features

 * Save, query, or delete models with a single line.
 * Chain SQL functions together for more control over your queries.
 * Automatically infers information such as table names, column names, and more.


## Usage

Start by extending `io.seanbailey.sqladapter.Model` on any of your models.

```java
import io.seanbailey.sqladapter.Model;

public class User extends Model {
  // ...
}
```

### Querying

Where
