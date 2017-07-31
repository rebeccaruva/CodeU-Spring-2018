#include <fstream>
#include <cstdlib>
#include "BTree.h"

using namespace std;

int main(int argc, char *argv[])
{
  BTree tree(atoi(argv[2]), atoi(argv[3])); // BTree with parameters M and L
  int value;

  // pass in values from text file
  ifstream inf(argv[1]);
  while(inf >> value)
  {
    tree.insert(value);
  } // while

  tree.print(); // print tree
  return 0;

}  // main
