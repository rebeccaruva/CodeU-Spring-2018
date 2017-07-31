#include <iostream>
#include "InternalNode.h"

using namespace std;

InternalNode::InternalNode(int ISize, int LSize,
  InternalNode *p, BTreeNode *left, BTreeNode *right) :
  BTreeNode(LSize, p, left, right), internalSize(ISize)
{
  keys = new int[internalSize]; // keys[i] is the minimum of children[i]
  children = new BTreeNode* [ISize]; // creates array of children
  leafSize = LSize;
} // InternalNode::InternalNode()


int InternalNode::getMinimum()const
{
  if(count > 0)   // should always be the case
    return children[0]->getMinimum(); // minimum of internal node is minimum of first leaf node
  else
    return 0;
} // InternalNode::getMinimum()


InternalNode* InternalNode::createNewLeafNode(BTreeNode* newNode, int i){
    // create new leaf node if current leaf nodes are full
    if((children[i-1])->getRightSibling() != NULL){ // indicates that leaf nodes are full
      ((children[i-1])->getRightSibling())->setLeftSibling(newNode); // create new leaf node between current leaf node and its right sibling
    }
    // set siblings and parents of split leaf nodes
    newNode->setRightSibling((children[i-1])->getRightSibling());
    (children[i-1])->setRightSibling(newNode);
    newNode->setLeftSibling((children[i-1]));
    newNode->setParent(parent);

    // shift keys and children over for new node
    for(int j = count-1; j>=i; j--){
      children[j+1] = children[j];
      keys[j+1] = keys[j];
    }
    children[i] = newNode;
    keys[i] = newNode->getMinimum();
    keys[i-1] = (children[i-1])-> getMinimum();

    count++; // one new child

    return NULL;
}

void InternalNode::sortInternalNodes(BTreeNode* node){
  // add new internal node into correct sorted position
  if(keys[count-1] <= node->getMinimum()){
    children[count] = node;
    keys[count] = node->getMinimum();
    count++;
    return;
  }

  for(int i = count - 1; i >= 0; i--){
    if(keys[i] >= node->getMinimum()){
        children[i+1] = children[i];
        keys[i+1] = keys[i];
    }
    else if (keys[i] <= node->getMinimum()) {
      children[i + 1] = node;
      keys[i + 1] = node->getMinimum();
      count++;
      return;
    }

    if(i == 0){
      children[0] = node;
      keys[0] = node->getMinimum();
      count++;
      return;
    }
  }
}

InternalNode* InternalNode::splitInternalNode(){
  // split internal nodes if full
  InternalNode* newNode = new InternalNode(internalSize, leafSize, NULL, NULL, NULL);

  int temp_key = -1;
  int position = 0;

  // find where to put new internal node
  if(internalSize % 2 == 0){
    while(count > internalSize/2 && position <= internalSize - 1){
      BTreeNode* temp = children[internalSize - position];
      temp_key = keys[internalSize - position];
      children[internalSize - position] = NULL;
      keys[internalSize - position] = -1;
      if(position != 0){ temp->setParent(newNode); newNode->sortInternalNodes(temp);}
      else{ temp->setParent(newNode); newNode->children[0] = temp; newNode->keys[0] = temp_key; (newNode->count)++; }
      position++;
      if(position > 1) count--;
    }
  } else{
    while(count > (internalSize/2 + 1) && position <= internalSize - 1){
      BTreeNode* temp = children[internalSize - position];
      temp_key = keys[internalSize - position];
      children[internalSize - position] = NULL;
      keys[internalSize - position] = -1;
      if(position != 0){ temp->setParent(newNode); newNode->sortInternalNodes(temp);}
      else{ temp->setParent(newNode); newNode->children[0] = temp; newNode->keys[0] = temp_key; (newNode->count)++; }
      position++;
      if(position > 1) count--;
    }
  }


  //cout << "new internal node: " << endl;
  //newNode->printSiblings();
  return newNode;
}

InternalNode* InternalNode::checkSiblings(){
  // check if either sibling has room and move child over if possible; otherwise return node
  if(leftSibling != NULL && leftSibling->getCount() <= internalSize - 1){
    ((InternalNode*)leftSibling)->sortInternalNodes(children[0]);

    for(int i = 1; i <= count; i++){
      children[i - 1] = children[i];
      keys[i - 1] = keys[i];
    }
  } else if(rightSibling != NULL && rightSibling->getCount() <= internalSize - 1){
    ((InternalNode*)rightSibling)->sortInternalNodes(children[count]);
    children[count] = NULL;
    keys[count] = -1;
    return this;
  } else{
    return splitInternalNode();
  }

  return NULL;
}

InternalNode* InternalNode::insert(int value)
{
  int i = 1;

  for(; keys[i] <= value && i < count; i++);

  int minimum = -1;

  // insert value and change minimum if needed
  if(children[i-1]->getRightSibling() != NULL){
    minimum = ((children[i-1])->getRightSibling())->getMinimum();
  }

  BTreeNode* newNode = children[i-1]->insert(value);

  if(newNode != NULL){
    if(newNode->getRightSibling() != NULL && ((newNode->getRightSibling())->getMinimum()) != minimum){
      if(children[i] != NULL){keys[i] = children[i]->getMinimum(); }
      else{
        ((InternalNode*)(rightSibling))->insert(this);
        return this;
      }
    }
    else {
      if(count <= internalSize - 1){ // create new leaf node if room
          return createNewLeafNode(newNode, i);
      }
      else if(this->leftSibling == NULL && this->rightSibling == NULL){ // if no room split node
        createNewLeafNode(newNode, i);
        count--;
        return splitInternalNode();
      }
      else if(this->leftSibling != NULL || this->rightSibling != NULL){ // if has siblings check those before splitting
        createNewLeafNode(newNode, i);
        count--;
        return checkSiblings();
      }
    }
  } else{ // otherwise change keys
      keys[i-1] = children[i-1]->getMinimum();
      return NULL;
  }

  return NULL; // to avoid warnings for now.
} // InternalNode::insert()

void InternalNode::insert(BTreeNode *oldRoot, BTreeNode *node2)
{
  // change root to new root and add old root and node as children

  oldRoot->setRightSibling(node2);
  node2->setLeftSibling(oldRoot);
  oldRoot->setParent(this);
  node2->setParent(this);
  children[0] = oldRoot;
  children[1] = node2;
  keys[0] = oldRoot->getMinimum();
  keys[1] = node2->getMinimum();
  count += 2;
} // InternalNode::insert()

void InternalNode::insert(BTreeNode *newNode) // from a sibling
{
  keys[0] = children[0]->getMinimum(); // if insert from sibling just change minimum
} // InternalNode::insert()

void InternalNode::print(Queue <BTreeNode*> &queue)
{
  int i;

  cout << "Internal: ";
  for (i = 0; i < count; i++)
    cout << keys[i] << ' ';
  cout << endl;

  for(i = 0; i < count; i++)
    queue.enqueue(children[i]);

} // InternalNode::print()
