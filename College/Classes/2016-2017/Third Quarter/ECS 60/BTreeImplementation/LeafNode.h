#ifndef LeafNodeH
#define LeafNodeH

#include "BTreeNode.h"

class LeafNode:public BTreeNode
{
  int *values;
  int leafSize;
public:
  LeafNode(int LSize, InternalNode *p, BTreeNode *left,
    BTreeNode *right);
  int getMinimum() const;
  LeafNode* insert(int value); // returns pointer to new Leaf if splits
  // else NULL
  void print(Queue <BTreeNode*> &queue);
  void sortValues(int value);
  LeafNode* leafNotFull(int value);
  LeafNode* splitLeaf(int value);
  LeafNode* checkSiblings(int value);
}; //LeafNode class

#endif
