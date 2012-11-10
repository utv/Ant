#clean
rm MyBot.jar

#package
jar cvfm MyBot.jar Manifest.txt *.class
 
#move to test folder
mv MyBot.jar /home/amata/aichallenge

#change myBot's permission
chmod u+x /home/amata/aichallenge/MyBot.jar

