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
  if(count > 0)  // should always be the case
    return values[0];
  else
    return 0;

} // LeafNode::getMinimum()

void LeafNode::sortValues(int value){
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

/*void LeafNode::printSiblings(){
  cout << "Leaf left sibling = ";
  if(leftSibling != NULL) cout << leftSibling->getMinimum() << endl;
  else cout << "-1" << endl;
  cout << "Leaf right sibling = ";
  if(rightSibling != NULL) cout << rightSibling->getMinimum() << endl;
  else cout << "-1" << endl;
  cout << "Parent = ";
  if(parent != NULL) cout << parent->getMinimum() << endl;
  else cout << "-1" << endl;
}*/

LeafNode* LeafNode::leafNotFull(int value){
  //cout << "in leaf not full" << endl;
  if(count != 0){
      sortValues(value);
  }
  else{
    count++;
    values[0] = value;
  }

  //cout << "minimum of leaf after insert = " << getMinimum() << endl;
  //printSiblings();
  return NULL;
}

LeafNode* LeafNode::splitLeaf(int value){
  //cout << "in split leaf" << endl;
  LeafNode* newNode = new LeafNode(leafSize, NULL, NULL, NULL);

  sortValues(value);
  count--;

  int temp = -1;
  int position = 0;

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

  //cout << "minimum of leaf after insert = " << getMinimum() << endl;
  //printSiblings();

  return newNode;
}

LeafNode* LeafNode::checkSiblings(int value){
  if(leftSibling != NULL && leftSibling->getCount() <= leafSize - 1){
    //cout << "inserting into left sibling" << endl;
    sortValues(value);
    count--;
    leftSibling->insert(values[0]);
    for(int i = 1; i <= count; i++){
      values[i - 1] = values[i];
    }
    //cout << "minimum of leaf after insert = " << getMinimum() << endl;
    //cout << "minimum of left leaf after insert = " << leftSibling->getMinimum() << endl;
    //printSiblings();
  } else if(rightSibling != NULL && rightSibling->getCount() <= leafSize - 1){
    //cout << "inserting into right sibling" << endl;
    //cout << "minimum of right leaf after insert = " << rightSibling->getMinimum() << endl;
    sortValues(value);
    count--;
    rightSibling->insert(values[count]);
    values[count] = -1;
    //cout << "minimum of leaf after insert = " << getMinimum() << endl;
    //printSiblings();
    return this;
  } else{
    //cout << "in last if statement of checkSiblings leaf" << endl;
    return splitLeaf(value);
  }

  return NULL;
}

LeafNode* LeafNode::insert(int value)
{
  //cout << "minimum of leaf before insert = " << getMinimum() << endl;
  if(count <= leafSize - 1){
    //cout << "in first if of insert leaf" << endl;
    return leafNotFull(value);
  }
  else if(this->leftSibling == NULL && this->rightSibling == NULL){
    //cout << "in second if of insert leaf" << endl;
    return splitLeaf(value);
  }
  else if(this->leftSibling != NULL || this->rightSibling != NULL){
    //cout << "in third if of insert leaf" << endl;
    return checkSiblings(value);
  }


  return NULL; // to avoid warnings for now.
}  // LeafNode::insert()

void LeafNode::print(Queue <BTreeNode*> &queue)
{
  cout << "Leaf: ";
  for (int i = 0; i < count; i++)
    cout << values[i] << ' ';
  cout << endl;
} // LeafNode::print()
