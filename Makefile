all: java

java: Assign5.java
	javac Assign5.java CPUScheduler.java DoublyLinkedList.java IOSystem.java Node.java FileRead.java

clean:
	rm -f *.class
