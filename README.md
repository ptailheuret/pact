# pact

Projet PACT Télécom ParisTech
-----------------------------

**Traitement d'images**

Implémentation d'une estimation de d'un cercle à partir d'une liste de points via un algorithme robuste RANSAC(1).

Obtention de cette liste de point sur une image à partir d'une estimation de contours suivant deux algorithmes:
  * Algorithme par gradient avec le filtre de SOBEL(2)
  * Algorithme par gradient avec le filtre de CANNY(3)
  

Références:
* (1): http://en.wikipedia.org/wiki/RANSAC
* (2): http://en.wikipedia.org/wiki/Sobel_operator
* (3): http://en.wikipedia.org/wiki/Canny_edge_detector
