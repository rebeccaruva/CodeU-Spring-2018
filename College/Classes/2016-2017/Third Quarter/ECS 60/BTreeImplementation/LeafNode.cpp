#include <iostream>
#include "LeafNode.h"
#include "InternalNode.h"
#include "QueueAr.h"

using namespace std;


LeafNode::LeafNode(int LSize, InternalNode *p,
  BTreeNode *left, BTreeNode *right) : BTreeNode(LSize, p, left, right)
{
  values = new int[LSize + 1];
  leafSize = LSize;
}  // LeafNode()


int LeafNode::getMinimum()const
{
  if(count > 0)  // return first value in leaf
    return values[0];
  else
    return 0;

} // LeafNode::getMinimum()

void LeafNode::sortValues(int value){
  // sort leaf node values by increasing order to maintain minimum
  if(values[count-1] <= value){
    values[count] = value;
    count++;
    return;
  }
  for(int i = count - 1; i >= 0; i--){
    if(values[i] >= value){
        values[i+1] = values[i];
    }
    else if (values[i] <= value) {
      values[i + 1] = value;
      count++;
      return;
    }
    if(i == 0){
      values[0] = value;
      count++;
      return;
    }
  }
}

LeafNode* LeafNode::leafNotFull(int value){
  // if leaf not full insert and sort values
  if(count != 0){
      sortValues(value);
  }
  else{
    count++;
    values[0] = value;
  }

  return NULL;
}

LeafNode* LeafNode::splitLeaf(int value){
  // if need to split leaf create new leaf node and sort values
  LeafNode* newNode = new LeafNode(leafSize, NULL, NULL, NULL);

  sortValues(value);
  count--;

  int temp = -1;
  int position = 0;

  // add leaf node where it belongs
  if(leafSize % 2 == 0){
    while(count > leafSize/2 && position <= leafSize - 1){
      temp = values[leafSize - position];
      values[leafSize - position] = -1;
      if(position != 0) newNode->sortValues(temp);
      else{ newNode->values[0] = temp; (newNode->count)++; }
      position++;
      if(position > 1) count--;
    }
  } else{
    while(count > (leafSize/2 + 1) && position <= leafSize - 1){
      temp = values[leafSize - position];
      values[leafSize - position] = -1;
      if(position != 0) newNode->sortValues(temp);
      else{ newNode->values[0] = temp; (newNode->count)++; }
      position++;
      if(position > 1) count--;
    }
  }

  return newNode;
}

LeafNode* LeafNode::checkSiblings(int value){
  // check if left sibling has room and move child over if so
  if(leftSibling != NULL && leftSibling->getCount() <= leafSize - 1){
    sortValues(value);
    count--;
    leftSibling->insert(values[0]);
    for(int i = 1; i <= count; i++){
      values[i - 1] = values[i];
    }
  } else if(rightSibling != NULL && rightSibling->getCount() <= leafSize - 1){
    /// check if right sibling has room and  move child over if so
    sortValues(value);
    count--;
    rightSibling->insert(values[count]);
    values[count] = -1;
    return this;
  } else{
    // split leaf if no room in siblings
    return splitLeaf(value);
  }

  return NULL;
}

LeafNode* LeafNode::insert(int value)
{
  // insert into leaf node if room
  if(count <= leafSize - 1){
    return leafNotFull(value);
  }
  else if(this->leftSibling == NULL && this->rightSibling == NULL){
    // split leaf if no siblings
    return splitLeaf(value);
  }
  else if(this->leftSibling != NULL || this->rightSibling != NULL){
    // check siblings before splitting
    return checkSiblings(value);
  }


  return NULL; 
}  // LeafNode::insert()

void LeafNode::print(Queue <BTreeNode*> &queue)
{
  cout << "Leaf: ";
  for (int i = 0; i < count; i++)
    cout << values[i] << ' ';
  cout << endl;
} // LeafNode::print()
