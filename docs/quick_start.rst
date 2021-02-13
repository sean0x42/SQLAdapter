Quick Start
===========

This guide will show you how to get up and running with the **SQL Adapter**.


Query Chaining
--------------

Query chaining is one of **SQL Adapter**'s most powerful features. It allows you
to build a partial query, and chain additional calls to build the exact

.. code-block:: java
   :linenos:

   Query query = User.all();
   System.out.println(query.toString());
   // SELECT * FROM Users;
