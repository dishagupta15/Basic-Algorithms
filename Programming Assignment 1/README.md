# Programming Assignment 1

This assignment implements the 2-3 tree data structure. This is the prompt:

While Arthur is spending time trying to get back to Earth in different dimensions, Ford has been thinking of crashing the different planets made by the Magrathean staff. Ford is too much of a frood to not have fun visiting any of them in the name of research for the guide.

He found you sitting in a bar drinking a pan galactic gargle blaster, blown out of your mind and talked to you about the planet database of the Magratheans. He would like to know the entrance fees of all the planets between some planet names. Help him get the information when the hangover passes.

There will be a single type of query to the database

- given two planet names return the list of planet names and entrance fees of all planets between the two queried names, in lexicographic order

Input Format

The first line contains a number n, which is the size of the database.

The next n lines are of the form

- a k

a - the planet name, k - the entrance fee of the planet, separated by a single space.

The next line contains a number  the number of queries to the database.

The next m lines are of the form

- a b

a, b - planet names (which names might not exist in the database).

Sample Input 0

3
earth 10
venus 30
jupiter 5
2
earth jupiter
earth venus

Sample Output 0

earth 10
jupiter 5
earth 10
jupiter 5
venus 30

Sample Input 1

3
earth 10
venus 30
jupiter 5
2
a z
venus earth

Sample Output 1

earth 10
jupiter 5
venus 30
earth 10
jupiter 5
venus 30
