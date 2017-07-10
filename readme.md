# pdfChain : blockchain for the masses

## what is a blockchain?

A blockchain is a distributed database that is used to maintain a continuously growing list of records, called blocks. 
Each block contains a timestamp and a link to a previous block. 
A blockchain is typically managed by a peer-to-peer network collectively adhering to a protocol for validating new blocks. 
By design, blockchains are inherently resistant to modification of the data. 
Once recorded, the data in any given block cannot be altered retroactively without the alteration of all subsequent blocks and a collusion of the network majority. 
Functionally, a blockchain can serve as "an open, distributed ledger that can record transactions between two parties efficiently and in a verifiable and permanent way. 
The ledger itself can also be programmed to trigger transactions automatically."

## why should you use it?

A blockchain supercedes older techonology that deals with authentication and non-repudiation.
First, there are many ways you can sign a document.
Typically by "signing" we mean creating a hash of a document and storing it.
With a blockchain the useful part is that once such a hash is stored it can not be changed or deleted. This gives you two things:

1. The hash itself identify the file from which it was computed
2. The fact that your hash is in the blockchain gives you a point in time when the operation was done.

Later you can say: 
Hey, I’ve created this hash on 10 Oct 2016 here is the transaction in the blockchain which contains the hash. I’ve created it according to this formula from this file. 
Now a person can take your file and compute the hash again and verify that it matches the one stored in the blockchain. 
All this works because:

1. It is very easy to compute the hash from a file but very difficult to craft a similar file which will produce exactly the same hash.
2. It is practically impossible to change the data stored inside blockchain.
3. Every transaction in the blockchain has a timestamp so having the transaction we know when exactly it was done.

The default iText implementation, specifically geared towards pdf documents stores:
 - a hash value of the document
 - the name of the algorithm that was used for hashing
 - a signed hash value of the document
 - the name of the algorithm that was used for signing
 - the pdf ID array
 
This allows you not only to store hash values of documents, but also to digitally sign them.
Being able to swap the hashing algorithm (in case of hashing algorithms becoming outdated) enables LTV (long term validation).

## what does iText provide?

### interfaces that hide abstraction

### concrete implementation using JSON-RPC and MultiChain

## example(s)

## how can you extend upon it?

## conclusion

learn more at itextpdf.com