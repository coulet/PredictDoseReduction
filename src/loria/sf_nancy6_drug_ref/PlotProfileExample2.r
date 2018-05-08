#install.packages("RMySQL");
library("RMySQL");


filename="./data/profile/example.png"
png(filename,  width = 400, height = 500, units = "px")

table="dose_down_65ante_lab_p450_all_h"
attribute="rxcui"
var="hydrocortisone"
query=paste("SELECT DISTINCT IF(log10(rr)<0, CONCAT(\"*NO* \",cui), cui) as phenotype, ABS(log10(rr)) as abs, num_of_publi FROM user_x", table, sep=".")
query=paste(query, "p WHERE", attribute, sep=" ")
query=paste(query, var, sep="='")
query=paste(query, " AND cui NOT IN (\'Lot #\',\'Manufacturer\',\'Interpretation\',\'RESULT\',\'TEST NAME\',\'REFERENCE LAB\',\'Comment\',\'COMMENT\',\'Disclaimer\',\'Method\',\'Results\',\'Interpreted by\',\'INTERPRETATIONS\',\'INTERPRETATION\',\'Source\',\'Volume\',\'Patient Result\',\'Notes\',\'Test Name\',\'Test\',\'Description\',\'Reference Lab\',\'Lab data\',\'TESTINFORMATION\',\'SPECIMEN TYPE\',\'Preliminary\',\'Result\',\'Others (describe)\',\'Primer\',\'Dosage\',\'001\',\'Specimen\')", sep="'")
query=paste(query, "AND cui!=\"null\" ORDER BY abs DESC LIMIT 20;", sep=" ")

query

mydb = dbConnect(MySQL(), user='x', password='', dbname='user_x', host='xx.stanford.edu')
rs <- dbSendQuery(mydb, query)
data <- fetch(rs, n=-1)

    funProp2 <- function(testCol) {
    data[, testCol]/max(data[, testCol])
}

data$abs.prop<- funProp2("abs")
data$num_of_publi.prop<- funProp2("num_of_publi")

myLeftAxisLabs <- pretty(seq(0, max(data$abs), length.out = 10))
myRightAxisLabs <- pretty(seq(0, max(data$num_of_publi), length.out = 10))

myLeftAxisAt <- myLeftAxisLabs/max(data$abs)
myRightAxisAt <- myRightAxisLabs/max(max(data$num_of_publi),1)

op <- par(mar = c(19,3.3,1,3.3) + 0.1)
barplot(t(as.matrix(data[, c("abs.prop", "num_of_publi.prop")])),
  beside = TRUE, yaxt = "n", names.arg = data$phenotype,
  ylim=c(0, max(c(myLeftAxisAt, myRightAxisAt))),las=2, col = c("red3", "palegreen4"))

axis(2, at = myLeftAxisAt, labels = myLeftAxisLabs, col="red3")
mtext("ABS(log10(rr))",side=2,line=2.5, col="red3")
axis(4, at = myRightAxisAt, labels = myRightAxisLabs, col="palegreen4") 
mtext("Number of publications in PubMed",side=4,line=2.5, col="palegreen4") 

dev.off()

