#include <fstream>
#include <iostream>
#include <cstdlib>
#include <cstdio>
#include "BTree.h"

using namespace std;

int main(int argc, char *argv[])
{
  BTree tree(atoi(argv[2]), atoi(argv[3])); //create new BTree with parameters M and L
  int value;
  char s[80];

  // pass in values from text file
  ifstream inf(argv[1]);
  while(inf >> value)
  {
    cout << "Inserting " << value << ". \n";
    tree.insert(value);
    tree.print(); // prints tree at each insertion
    fgets(s, 80, stdin); // waits for some input before moving on to next value
  } // while

  tree.print(); // prints tree
  return 0;
}  // main
