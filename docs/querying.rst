Querying
========

Todo
----

  - toString()
  - where()
      - custom operators
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
   System.out.println(query.toString());

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
  :linenos:

  Query query = User.find("sean0x42");

Seems simple enough, right? That's because it is. This function is designed to retrieve
a single instance of your model, whose primary key matches the given value.

.. code-block:: sql
  :linenos:

  SELECT * FROM Users WHERE username = "sean0x42" LIMIT 1;


Find By
-------

But what about when you're searching for a specific model, but you don't know
the value of its primary key?
