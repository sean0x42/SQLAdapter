# SQL Adapter

[![GitLicense](https://gitlicense.com/badge/sean0x42/SQLAdapter)](https://gitlicense.com/license/sean0x42/SQLAdapter)
[![Documentation Status](https://readthedocs.org/projects/sqladapter/badge/?version=latest)](https://sqladapter.readthedocs.io/en/latest/?badge=latest)
[![Coverage Status](https://coveralls.io/repos/github/sean0x42/SQLAdapter/badge.svg?branch=master)](https://coveralls.io/github/sean0x42/SQLAdapter?branch=master)
[![Say Thanks!](https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg)](https://saythanks.io/to/sean0x42)

**SQL Adapter** is a powerful ORM for adapting Java objects into SQL and back
again. It is heavily inspired by
[ActiveModel](https://github.com/rails/rails/tree/master/activemodel) from Ruby
on Rails, but still maintains its own identity, and feels at home within the
Java ecosystem.

The original version of this library was throw together very quickly for a
university assignment by [Sean Bailey](https://www.seanbailey.io).


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

Then head over to the [documentation](https://sqladapter.readthedocs.io) to get
install and get started.


## Thanks

This library was battle tested by my university mates:

 * [Adam Crocker](https://github.com/patch7331)
 * [Jacob Nolan](https://github.com/JacobNolan1)
 * [Jack Parkes](https://github.com/JackParkes1)
