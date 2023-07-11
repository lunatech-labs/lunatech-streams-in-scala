# Streams in Scala
This is a sample project that accompanies the blog post [Stream in Scala](https://blog.lunatech.com/stream-in-scala/).

<br/>
A simple application that reads log files and prints the lines to the console using scala LazyList and Akka Stream.
The project is divided into two parts:
<br>
1. LogStream: This is just an abstraction wrapping LazyList to resists the temptation to use LazyList methods that can cause memory leaks.
<br>
2. AkkaStream: Here we use Akka Stream to read the log files and print the lines to the console.

### Setup the project
This project makes use of log files to demonstrate the use of LazyList and Akka Stream. The log files are not included in the project,
but can be downloaded from [here](https://zenodo.org/record/3227177). To run the project, you need to place the extracted log files in the following directory:
src/main/resources/logs


### How to run the application
1. Clone the project
2. Run `sbt run` in the root directory of the project


