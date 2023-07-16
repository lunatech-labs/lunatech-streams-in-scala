# Streams in Scala
This is a sample project that accompanies the blog post [Stream in Scala](https://blog.lunatech.com/stream-in-scala/).

<br/>
A simple application that reads log files and prints the lines to the console using scala LazyList and Akka Stream.
The project is divided into two parts:
<br>
1. LogStream: This is just an abstraction wrapping LazyList to resists the temptation to use LazyList methods that can cause memory leaks.
<br>
2. AkkaStream: Here we use Akka Stream to read the log files and print the lines to the console.
<br>
3. Fs2Stream: Here we use Fs2 Stream to read the log files and print the lines to the console.

### Setup the project
This project makes use of log files to demonstrate the use of LazyList and Akka Stream. The log files are not included in the project,
but can be downloaded from [here](https://zenodo.org/record/3227177). To run the project, you need to place the extracted log files in the following directory:
src/main/resources/logs


### How to run the application
1. Clone the project
2. Run `sbt run` in the root directory of the project

### Performance comparisons of LogStream, Akka Stream and Fs2 Stream
The following table shows the average time taken to read the log files and print the lines to the console using LazyList, Akka Stream and Fs2 Stream.
The tests were run on a MacBook Pro (Retina, 13-inch, Early 2015) with 2.7 GHz Dual-Core Intel Core i5 processor and 8 GB 1867 MHz DDR3 memory.

#### Recording the time
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
8. After running each stream type multiple times, the average time taken is calculated and recorded in the table below.

<br>
| Stream Type | Average Time Taken ( ms ) | Search String ( ERROR )   |
|-------------|---------------------------|---------------------------|
| LogStream   | 7158                      | ERROR                     |                         
| Akka Stream | 4218                      | ERROR                     |
| Fs2 Stream  | 6976                      | ERROR                     |
<br>




