# SAE 3.03 - Réseeau Social
Titouan Coulon

## Contexte

En JAVA, créer un serveur et un client pouvant communiquer, le serveur doit pouvoir communiquer avec plusieurs clients simultanement.  
Le but est de réaliser un réseau social où les utilisateurs peuvent poster des messages, se suivre et liker leurs posts.  
Le serveur communique en TCP avec les clients via java.net.Socket et utilise des Threads pour gérer les connexions simultanées.

## Ce que j'ai réussi à faire
Le serveur communique avec un ou plusieurs clients en simultané, il recoit les messages et les stockent dans un bd en json pour etre facilement exportable, lisible par tout les logiciels et persistante.  
Un utilisateur peut poster des messages qui seront envoyés à tout ses abonnés, qui pourront liker son message ou non, les utilisateurs peuvent se follow et s'unfollow avec une commande / dans le client.
Le serveur peut supprimer des utilisateurs et des messages. J'ai commencé une interface graphique mais j'ai manqué de temps et n'ai pu faire qu'une bribe du profil de l'utilisateur connecté.

## Ce que je n'ai pas réussi à faire
Les messages privés entre utilisateurs, l'interface graphique finale, voir les messages de facon plus pratique dans le terminal.

## Les compétences développées 
- Compétence 3 : Administrer des systèmes informatiques communicants complexes
- Compétence 6 : Travailler dans une équipe informatique
La competence 3 à bien été mise en oeuvre car le serveur et les clients communiquent sur le réseau via TCP étudié en cours.
Je n'ai malheuresement pas réussi à trouver un binome pour réaliser le travail et ai donc du le réaliser seul néanmoins j'ai veillé à laisser mon projet propre, maintenable, organisé avec des commentaires et des scripts de lancement pour faciliter l'usage.

# Manuel Utilisateur

## Lancement 
Le plus simple pour lancer le serveur est d'exécuter `sh serveur.sh`.  
Les commandes disponibles sur celui-ci sont :  
    - help : affiche toutes les commandes disponibles,  
    - stop : arrête le serveur,  
    - delete \<id\>: supprime un message,  
    - remove \<pseudo\>: supprime un utilisateur et tout ses messages,  
    - save : sauvegarde la base de données.  
Pour lancer un client, il faut exécuter `sh client.sh`, il sera demandé si il faut utiliser le fichier .client qui contient l'ip, le port et le pseudo de l'utilisateur ou non.  
Si le fichier n'est pas utilisé, l'utilisateur doit renseigner les informations lui-même. L'utilisateur peut désormais poster un message et recevra une confirmation que le message est bien arrivé.
Il peut également exécuter certaines commandes comme :   
    - search pour rechercher un utilisateur ou un message ( la syntaxe est indiquée dans l'application ),   
    - help pour obtenir toutes les commandes,   
    - follow \<pseudo\> pour suivre un utilisateur,  
    - unfollow \<pseudo\> pour arrêter de suivre un utilisateur,  
    - like \<id\> pour liker un message,  
    - unlike \<id\> pour arrêter de liker un message,  
    - reception pour recevoir les messages des ses abonnements,  
    - supprimer \<id\> pour supprimer un de ses messages,  