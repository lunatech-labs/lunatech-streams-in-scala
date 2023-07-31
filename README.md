# Streams in Scala
This is a sample project that accompanies the blog post [Streams in Scala](https://blog.lunatech.com/posts/2023-07-28-streams-in-scala--an-introductory-guide).

<br/>
A simple application that reads log files and prints the lines to the console using scala LazyList (LogStream), Akka Stream and Fs2 Stream.
The project is divided into three parts:
<br>
1. LogStream: This is just an abstraction wrapping LazyList to resists the temptation to use LazyList methods that can cause memory leaks.
<br>
2. AkkaStream: Here we use Akka Stream to read the log files and print the lines to the console.
<br>
3. Fs2Stream: Here we use Fs2 Stream to read the log files and print the lines to the console.


### How to run the application
1. Clone the project
2. Run `sbt run` in the root directory of the project




