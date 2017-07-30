#include <iostream>
#include "InternalNode.h"

using namespace std;

InternalNode::InternalNode(int ISize, int LSize,
  InternalNode *p, BTreeNode *left, BTreeNode *right) :
  BTreeNode(LSize, p, left, right), internalSize(ISize)
{
  keys = new int[internalSize]; // keys[i] is the minimum of children[i]
  children = new BTreeNode* [ISize];
  leafSize = LSize;
} // InternalNode::InternalNode()


int InternalNode::getMinimum()const
{
  if(count > 0)   // should always be the case
    return children[0]->getMinimum();
  else
    return 0;
} // InternalNode::getMinimum()


InternalNode* InternalNode::createNewLeafNode(BTreeNode* newNode, int i){
    //cout << "entered create new leaf" << endl;
    if((children[i-1])->getRightSibling() != NULL){
      ((children[i-1])->getRightSibling())->setLeftSibling(newNode);
    }
    newNode->setRightSibling((children[i-1])->getRightSibling());
    (children[i-1])->setRightSibling(newNode);
    newNode->setLeftSibling((children[i-1]));
    newNode->setParent(parent);


    for(int j = count-1; j>=i; j--){
      children[j+1] = children[j];
      keys[j+1] = keys[j];
    }
    children[i] = newNode;
    keys[i] = newNode->getMinimum();
    keys[i-1] = (children[i-1])-> getMinimum();

    //printSiblings();
    //cout << "minimum of new leaf after insert = " << newNode->getMinimum() << endl;
    //newNode->printSiblings();

    count++;
    return NULL;
}

void InternalNode::sortInternalNodes(BTreeNode* node){
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
  //cout << "Entered split internal node" << endl;
  InternalNode* newNode = new InternalNode(internalSize, leafSize, NULL, NULL, NULL);

  int temp_key = -1;
  int position = 0;

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
  if(leftSibling != NULL && leftSibling->getCount() <= internalSize - 1){
    //cout << "entered left sibling check" << endl;
    ((InternalNode*)leftSibling)->sortInternalNodes(children[0]);
    //cout << "inserted into left sibling" << endl;
    for(int i = 1; i <= count; i++){
      children[i - 1] = children[i];
      keys[i - 1] = keys[i];
    }
    //printSiblings();
  } else if(rightSibling != NULL && rightSibling->getCount() <= internalSize - 1){
    ((InternalNode*)rightSibling)->sortInternalNodes(children[count]);
    children[count] = NULL;
    keys[count] = -1;
    //printSiblings();
    return this;
  } else{
    return splitInternalNode();
  }

  return NULL;
}

InternalNode* InternalNode::insert(int value)
{
  // students must write this
  int i = 1;

  for(; keys[i] <= value && i < count; i++);
  //cout<< "finished for loop" << endl;
  int minimum = -1;
  if(children[i-1]->getRightSibling() != NULL){
    minimum = ((children[i-1])->getRightSibling())->getMinimum();
  }

  BTreeNode* newNode = children[i-1]->insert(value);
  //cout << "entering node into children" << endl;
  //cout << "i = " << i << endl;
  //cout << "count = " << count << endl;;
  //cout << "children[i] = " << endl;

  //cout << "keys[i] = " << keys[i] << endl;

  if(newNode != NULL){
    if(newNode->getRightSibling() != NULL && ((newNode->getRightSibling())->getMinimum()) != minimum){
      //cout << "entered first if" << endl;
      if(children[i] != NULL){keys[i] = children[i]->getMinimum(); }
      else{
        ((InternalNode*)(rightSibling))->insert(this);
        //printSiblings();
        return this;
      }
    }
    else {
      if(count <= internalSize - 1){
          //cout << "entered insert into node" << endl;
          return createNewLeafNode(newNode, i);
      }
      else if(this->leftSibling == NULL && this->rightSibling == NULL){
        //cout << "entered no siblings" << endl;
        createNewLeafNode(newNode, i);
        count--;
        return splitInternalNode();
      }
      else if(this->leftSibling != NULL || this->rightSibling != NULL){
        //cout << "entered has siblings" << endl;
        createNewLeafNode(newNode, i);
        count--;
        return checkSiblings();
      }
    }
  } else{
      keys[i-1] = children[i-1]->getMinimum();
      //printSiblings();
      return NULL;
  }

  return NULL; // to avoid warnings for now.
} // InternalNode::insert()

/*void InternalNode::printSiblings(){
  cout << "internal node " << getMinimum() << " children" << endl;
  cout << "Internal left sibling = ";
  if(leftSibling != NULL) cout << leftSibling->getMinimum() << endl;
  else cout << "-1" << endl;
  cout << "Internal right sibling = ";
  if(rightSibling != NULL) cout << rightSibling->getMinimum() << endl;
  else cout << "-1" << endl;
  cout << "Parent = ";
  if(parent != NULL) cout << parent->getMinimum() << endl;
  else cout << "-1" << endl;
  for(int i = 0; i < count; i++){
    cout << i << endl;
    (children[i]->printSiblings());
    cout << keys[i] << endl;
  }
}*/
void InternalNode::insert(BTreeNode *oldRoot, BTreeNode *node2)
{ // Node must be the root, and node1
  // students must write this
  /*if((children[i-1])->getRightSibling() != NULL){
    ((children[i-1])->getRightSibling())->setLeftSibling(newNode);
  }
  newNode->setRightSibling((children[i-1])->getRightSibling());
  (children[i-1])->setRightSibling(newNode);
  newNode->setLeftSibling((children[i-1]));
  newNode->setParent(this);

  for(int j = count-1; j>=i; j--){
    children[j+1] = children[j];
    keys[j+1] = keys[j];
  }
  children[i] = newNode;
  keys[i] = newNode->getMinimum();

  count++;*/

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
  keys[0] = children[0]->getMinimum();
} // InternalNode::insert()*/

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
