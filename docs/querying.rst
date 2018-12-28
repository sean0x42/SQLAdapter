Querying
========

Todo
----

  - order()
  - limit()
  - offset()
  - page() and per()
  - count()
  - exists()


All
---

You can retrieve all instances of any given model using the :code:`all()`
function. This function returns a :code:`Query` object, so it's a fantastic way
to start query chains without defining any initial conditions.

.. code-block:: java
   :linenos:

   Query query = User.all();
   System.out.println(query.getPreview());

   // => SELECT * FROM Users;


Find
----

The :code:`find(Object)` method is used for locating a specific instance of a
model according to its primary key. By default, this is the first field on any
given model.

.. code-block:: java
  :linenos:

  public class User extends Model {

    private Integer id; // Primary key since it is defined first
    private String username;

    // Class truncated...
  }

However, we recommend that you override this functionality using the
:code:`@PrimaryKey` annotation. Example below.

.. code-block:: java
  :linenos:

  import io.seanbailey.sqladapter.model.PrimaryKey;

  public class User extends Model {
    
    private Integer id;

    @PrimaryKey
    private String username; // Now this is the primary key

    // Class truncated...
  }

Now that you know which field is considered the primary key, the easiest way to
see how the :code:`find(Object)` method works is to see it in action.

.. code-block:: java

  Query query = User.find("sean0x42");

Seems simple enough, right? That's because it is. This function is designed to
retrieve a single instance of your model, whose primary key matches the given
value.

.. code-block:: sql

  SELECT * FROM Users WHERE username = "sean0x42" LIMIT 1;


Find By
-------

Sometimes it's useful to find an instance of your model with a field other than
the primary key. The :code:`findBy(String, Object)` method is useful in such
situations.

.. code-block:: java

  Query query = User.findBy("email", "alex@example.com");

The resulting SQL is almost identical to that of :code:`find(Object)`, but we
have control over which column the condition is defined for.

.. code-block:: sql

  SELECT * FROM Users WHERE email = "alex@example.com" LIMIT 1;


Where
-----

The :code:`where(String, Object)` function retrieves a collection of instances
which match the given condition.

.. code-block:: java

  Query query = User.where("admin", true);

The above example would produce the following SQL:

.. code-block:: sql

  SELECT * FROM Users WHERE admin = true;


The Power of Chaining
~~~~~~~~~~~~~~~~~~~~~

Because :code:`where(String, Object)` returns a :code:`Query` object, we can
always chain additional calls and narrow down our resulting collection.

.. code-block:: java

  User.where("admin", true).and("score", 400);

When you chain :code:`where` calls in this way, SQL Adapter assumes that you
would like to join these conditions with the :code:`AND` boolean operator. So
the above Java would produce:

.. code-block:: sql

  SELECT * FROM Users WHERE admin = true AND score = 400;

The :code:`or(String, Object)` function produces the same effect, but using the
:code:`OR` boolean operator instead.

.. code-block:: java

  Users.where("admin", true).or("score", 400);

.. code-block:: sql

  SELECT * FROM Users WHERE admin = true OR score = 400;


Custom Operators
~~~~~~~~~~~~~~~~

SQL Adapter also allows you to explicitly declare which operators should be used
within each condition.

.. code-block:: java
  
  User.where("score >= ?", 400); 

.. code-block:: sql

  SELECT * FROM Users WHERE score >= 400;

This functionality is also available to the :code:`findBy(String, Object)`
method.



