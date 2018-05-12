# Starting a query chain

The following functions are available for all models, and will create a new SQL chain. Combine these functions with the [chain functions](#chain-functions) below to create some powerful SQL queries.

## All

The `Model::all` function starts an SQL chain, that can be used to retrieve every model. Usage:

```java
Issue[] issues = Issue.all().execute();
```

This will generate the following SQL:

```SQL
SELECT * FROM issues;
```


## Where

Use the `Model::where` function when you'd like to retrieve multiple models that match a particular condition. Usage:

```java
User bob = User.find(14).execute();
Issue[] issue = Issue.where("authorId", bob).execute();
```

With the above Java, the SQL adapter infers that you want to find instances of `Issue` where the `authorId` is equal to the primary key of the `User` bob. This generates the following SQL:

```SQL
SELECT * FROM issues WHERE authorId = ?; // [14]
```

Notice that the SQL generated uses prepared statements rather than directly injecting the string. This prevents SQL injection attacks.

#### Extended syntax 

You may also use a more extended syntax, which can be useful when dealing with dates or comparisons.

```java
Issue[] issue = Issue.where("createdAt < ?", new Date()).execute();
```

Which becomes:

```SQL
SELECT * FROM issues WHERE createdAt < ?; // ['2018-05-09']
```


## Find

The `Model::find` function can be used for querying a single object in the database. You may use any unique attribute, but by default the adapter attempts to find a primary key.

```java
Issue issue = Issue.find(14).execute();
```

Which creates this SQL:

```SQL
SELECT * FROM issues WHERE id = ? LIMIT 1; // [14]
```


# Chain Functions

These functions can be used on SQL chains, but will not result in a call to the database. When you're ready to hit the db, use one of the [finisher moves](#finisher-moves) below.


## Order

Ordering elements is easy with `SQLChain::order`. Just use the following syntax:

```java
Issue[] issues = Issue.all()
                      .order("createdAt", Order.DESC)
                      .execute();
```

This will give us the following SQL:

```SQL
SELECT * FROM issues ORDER BY createdAt DESC;
```

You can of course chain multiple `SQLChain::order` functions together.

```java
Player[] players = Player.all()
                         .order("score", Order.DESC)
                         .order("username", Order.ASC)
                         .execute();
```

Which allows us to create a hierarchy of order calls. This would generate SQL like the following:

```SQL
SELECT * FROM players ORDER BY score DESC, username ASC;
```


## Limit

This function allows you to limit the total number of models that are retrieved in this query. It is often quite efficient to use such a call, as the DBMS has to check fewer rows.

```java
Game[] games = Game.all().limit(25).execute();
```

As you might've guessed, you'll get the following SQL:

```SQL
SELECT * FROM games LIMIT 25;
```


## Offset

The `SQLChain::offset` function can be used in conjunction with `SQLChain::limit`, or on it's own. In essence, it sets the starting row to something further down the table. 

```java
Season fifthSeason = Season.all().limit(1).offset(5).execute();
```

This gives the following SQL

```SQL
SELECT * FROM seasons LIMIT 1 OFFSET 5;
```


## Where

`SQLChain::where` is similar to `Model::where`, but adds additional conditions that the model must conform to. Consider this second where to be kind of like an AND function.

```java
Student[] aussieGraduates = Student.where("state", Student.State.GRADUATED)
                                   .where("country", Student.Country.AUSTRALIA)
                                   .execute();
```

Which produces:

```SQL
SELECT * FROM students WHERE state = ? AND country = ?;
// ['GRADUATED', 'AUSTRALIA']
```


## Or

`SQLChain::or` is similar to `SQLChain::where`, but adds an optional (or) condition.

```java
Student[] aussieOrKiwi = Student.where("country", Student.Country.AUSTRALIA)
                                .where("country", Student.Country.NEW_ZEALAND)
                                .execute();
```

Generates:

```SQL
SELECT * FROM students WHERE country = ? OR country = ?;
// ['AUSTRALIA', 'NEW_ZEALAND']
```


## Page and Per

`SQLChain::page` and `SQLChain::per` are utility functions that can be used for pagination. You may use them like so:

```java
Country[] countries = Country.all().page(5).per(15);
```

And the SQL it generates:

```SQL
SELECT * FROM countries LIMIT 15 OFFSET 75;
```



# Finisher Moves

The following functions can only be called on an SQL chain. In other words, they depend upon one of the above functions. These functions are unique in that they will execute a call to the database and end the chain.

## Count

`SQLChain::count` returns the number of instances in the database. Pretty straight forward. You should only use this with either `Model::all` or `Model::where`. Check the existence of a model with `SQLChain::exists`.

```java
int issues = Issue.all().count();
```

This would result in this SQL:

```SQL
SELECT COUNT(*) FROM issues;
```

## Exists

You may use `SQLChain::exists` to determine whether any instances of the model exist. 

> **Note:** You usually want to avoid using this function. If you're dealing with a case where you need to retrieve multiple elements, but the set could be empty, simply handle an empty array rather then using this check. This prevents an additional unnecessary database hit.

```java
boolean found = Issue.find("title", "Alphabet soup on toast").exists();
```

This generates the following SQL:

```SQL
SELECT COUNT(*) FROM issues WHERE title = ? LIMIT 1; // ["Alphabet soup on toast"]
```


## Execute

Use `SQLChain::execute` to make a call to the database with the current chain. It doesn't really do anything special. 





# Saving



# Event Handling