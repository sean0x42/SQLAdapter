# SQL Adapter

[![GitLicense](https://gitlicense.com/badge/sean0x42/SQLAdapter)](https://gitlicense.com/license/sean0x42/SQLAdapter)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/sean0x42/SQLAdapter.svg)](https://github.com/sean0x42/SQLAdapter)
[![GitHub issues](https://img.shields.io/github/issues/sean0x42/SQLAdapter.svg)](https://github.com/sean0x42/SQLAdapter/issues/)
[![Coverage Status](https://coveralls.io/repos/github/sean0x42/SQLAdapter/badge.svg?branch=master)](https://coveralls.io/github/sean0x42/SQLAdapter?branch=master)
[![Say Thanks!](https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg)](https://saythanks.io/to/sean0x42)

An ORM for adapting Java objects into SQL and back again. Inspired by
ActiveRecord. This library was originally built very quickly to aid in a
university assignment.

Maintained by [Sean Bailey](https://www.seanbailey.io).


## Features

 * Save, query, or delete models with a single line.
 * Chain SQL functions together for more control over your queries.
 * Automatically infers information such as table names, column names, and more.
 * Highly configurable.


## Usage

Start by extending `io.seanbailey.sqladapter.Model` on any of your models.

```java
import io.seanbailey.sqladapter.Model;

public class User extends Model {
  // ...
}
```

Then head over to the [quick start guide](https://github.com/sean0x42/SQLAdapter/wiki/Quick-Start-Guide), or [read the documentation]() (coming soon) to get started.

## Thanks

This library was battle tested by my university mates:

 * [Adam Crocker](https://github.com/patch7331)
 * [Jacob Nolan](https://github.com/JacobNolan1)
 * [Jack Parkes](https://github.com/JackParkes1)
