# ParseGGPK
Parsing the GGPK file for Grinding Gear Games: Path of Exile

Aim is to have an executable with a simple UI that allows for *relatively* fast prefix searching within the directory.

Files found during search can then be extracted (or a complete extract can be run).

Ideally JUnit Jupiter testing classes will be included.

Documentation will be relatively extensive, but provided in a build (and test) -->refactor (and test)-->document rinse repeat cycle.

To do:
- Move home brew Trie tree implementation into the project. Done.
- Change the directory structure to store by name hash instead of offset. Not done, and might not get done as the as-is works in a more generic fashion.
- Find out why there are orphaned files such as WoodenPortal.aoc. Ongoing, issue raised at Omega.
- Link Trie tree to directory structure. Done.
- Build a simple UI. Done, but currently only allows browsing the GGPK. Not to mention that the location of the GGPK is hardcoded :)
-Extract specific files to specific folder. 


