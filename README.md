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
1. Using the following command: `sbt run`, you would be asked to select the stream type to use, available options are: 
```cmd
[(LogStream->l), (AkkaStream->a), (fs2Stream->f)]
```
2. Pick the letter `l` to use LogStream.
3. choose wether to use filter or by line number, available options are:
```cmd
[(filter->f), (line number->l)]
```
4. Pick the letter `f` to use filter.
5. Enter the filter string, check your log files to pick a filter string, for example: `ERROR`.
6. The program will print the lines that contain the filter string to the console after reading the log files with the time taken.
7. Repeat the process to use the other stream types.





