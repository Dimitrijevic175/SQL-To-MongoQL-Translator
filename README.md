# SQL-To-MongoQL-Translator

The project focuses on developing a tool for user interaction with MongoDB using SQL queries. The tool allows users to write SQL queries, which will be validated, parsed, and translated into the corresponding format (MongoQL). The translated query will then be executed against the MongoDB, and the resulting document collection will be structured for tabular display.

# GUI

Based on Swing, support user interaction with the database similar to the Live SQL tool. Provide a workspace where users can write code (TextArea) and a panel to display query results (JTable).

# Parser

The parser's role is to take a SQL query string and convert it into an object representation.

# Validator

The validator checks the created query to ensure basic SQL syntax rules are met, making further translation feasible.

# Adapter

Valid queries are passed to the adapter for translation. The Adapter component converts SQL queries into a MongoQuery object, preparing parameters and mapping them to MongoDB's format.

# Executor and Packager

The Executor component executes the MongoQuery against the MongoDB HR database server. The Packager translates unstructured MongoDB documents into a structured dataset suitable for display in JTable.