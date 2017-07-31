#include <iostream>
#include "BTree.h"
#include "BTreeNode.h"
#include "LeafNode.h"
#include "InternalNode.h"
#include <typeinfo>
using namespace std;

BTree::BTree(int ISize, int LSize):internalSize(ISize), leafSize(LSize)
{
  root = new LeafNode(LSize, NULL, NULL, NULL); // initialize root to empty leaf node
} // BTree::BTree()


void BTree::insert(const int value)
{
  BTreeNode* node = root->insert(value); // insert value
  if(node != NULL){ // if value requires new root
    // create new root that contains two children, current root and new node
    InternalNode* internalNode = new InternalNode(internalSize, leafSize, NULL, NULL, NULL);
    internalNode->insert(root, node);
    root = internalNode;
  }
} // BTree::insert()

void BTree::print()
{
  // prints BTree with level-order traversal using a queue
  BTreeNode *BTreeNodePtr;
  Queue<BTreeNode*> queue(1000);

  queue.enqueue(root);
  while(!queue.isEmpty())
  {
    BTreeNodePtr = queue.dequeue();
    BTreeNodePtr->print(queue);
  } // while
} // BTree::print()
