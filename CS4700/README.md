## Overview

This project handles DNS requests in 2 main ways. the first way is by answering questions on the project own domin by using the records found in the local zone. So that way we canreply to the request when the name belong to the domain. The second main way it handles requests is hwen the name of the domain is not its own. The server asks other DNS if the "known" the domain. It starts in the root server, then it goes through the referrals and keeps going till it finds. The server never stops running and can handle multiple requests. All the requests gets saved in the cache for future lookups and it if times out the record gets removed. 

## Challenges

The part I had the most issue with was the bailiwick and all the edge cases it. The DNS response can include extra records which was throwing my methods off and causing it to crash. Throught the process I had to make sure I only accepted records that belong to the server that sent them. It took some trial and error but once completed everything feel into place. Handling the cache and timing out old records also took work to get correct.

## Testing

I created a .sh script that rans all the configs and if it failed it showed the message on where it failed.
