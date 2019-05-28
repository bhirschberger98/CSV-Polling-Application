# Polling Data Analysis App

This application takes an Excel CSV file or Text (.txt) file containing a poll of names and tallies each name based on the way the name is styled and the committee the name is in. For example, if the poll contains the names "Dr. John Smith", "John", and "J. Smith" all belonging to the "Election Committee" then the program will give 3 tallies under "John Smith":

*Dr. John Smith,John,J. Smith --> outputs "[John Smith is in the Election committee and has 3 votes.]"*

But if there's a name such as "Jack Smith" in the poll belonging to the "Election Committee", then "J. Smith" will not be included in the the tallies of "John Smith" nor "Jack Smith":

*Dr. John Smith,John,J. Smith,Jack Smith --> outputs "[John Smith is in the Election committee and has 2 votes., J. Smith is in the Election committee and has 1 vote., Jack Smith is in the Election committee and has 1 vote.]"*

It also contains 2 CSV files titled "prefixes.csv" and "sufixes.csv" for recording any possible prefixes and suffixes that will need to be taken into account in the program.

## Tutorial
1. Drag an Excel CSV file or Text (.txt) file to where it says "Drop a File Here".
2. The program will then tally the names and display the results.

### Author(s)
- Brett Hirschberger

- Ivan Williams
