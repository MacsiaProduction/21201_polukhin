#include <stdlib.h>
#include <stdio.h>
typedef struct node node;

struct node // структура узлов дерева
{
	node *left;
	node *right;
	int key;
	unsigned char height;
};

node* init(node* NEW, int k)
{
    NEW->key = k;
    NEW->height = 1;
    NEW->left = 0;
    NEW->right = 0;
    return NEW;
}

unsigned char height(node* p)
{
	return p?p->height:0;
}

int bfactor(node* p)
{
	return height(p->right)-height(p->left);
}

void fix_height(node* p)
{
	unsigned char hl = height(p->left);
	unsigned char hr = height(p->right);
	p->height = (hl>hr?hl:hr)+1;
}

node* rotate_r(node* p) // правый поворот вокруг p
{
	node* q = p->left;
	p->left = q->right;
	q->right = p;
	fix_height(p);
	fix_height(q);
	return q;
}

node* rotate_l(node* q) // левый поворот вокруг q
{
	node* p = q->right;
	q->right = p->left;
	p->left = q;
	fix_height(q);
	fix_height(p);
	return p;
}

node* balance(node* p) // балансировка узла p
{
	if( bfactor(p)==2 )
	{
		if( bfactor(p->right) < 0 )
			p->right = rotate_r(p->right);
		return rotate_l(p);
	}
	if( bfactor(p)==-2 )
	{
		if( bfactor(p->left) > 0  )
			p->left = rotate_l(p->left);
		return rotate_r(p);
	}
	return p; // балансировка не нужна
}

node* insert(node* p, int k, node* NEW) // вставка ключа k в дерево с корнем p
{
	if( !p ) return init(NEW, k);
	if( k<p->key )
		p->left = insert(p->left, k, NEW);
	else
		p->right = insert(p->right, k, NEW);
	fix_height(p);
	return balance(p);
}

int main(void) {
    node *k = NULL;
    int n, j = 0;
    if ( 1 != scanf("%d", &n)) return 0;
	node* NEWtable = (node*)calloc(n, sizeof(node));
    for(int i = 0; i < n; i++) {
        int x;
        if ( 1 != scanf("%d", &x)) return 0;
        k = insert(k, x, &NEWtable[j++]);
    }
    printf("%d", height(k));
	free(NEWtable);
    return EXIT_SUCCESS;
}
