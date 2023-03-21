import java.util.ArrayList;
public class Scheduler {
	//	Asagida olusturulan waitList ve Queue'larin hepsi bagli liste yapisina sahiptir.
	SinglyLinkedList waitList = new SinglyLinkedList();
	Queue highPQueue = new Queue();
	Queue midPQueue = new Queue();
	Queue lowPQueue = new Queue();
	ArrayList<Resource> resourceList = new ArrayList<>();
	int zaman=0;
	
	public void setResourcesCount(int sayi) {
		//	Burasi verilen sayiya gore yeni resource olusturur.
		for(int i=1;i<sayi+1;i++) {
			resourceList.add(new Resource(i));
			}
		}
	
	public void add(job j) {
		//	Burasi her gelen job'u waitList'e ekler ve eklenme sonucunda da waitList'i sort eder.
		waitList.add(j);
		waitList.bubbleSort();
		}
	
	public void utilization(int sayi) {
		//	Kullanilan resource'un gecen zaman sirasinda ne kadar calistigina oranini verir.
		for(int i=0;i<resourceList.size();i++) {
			if(resourceList.get(i).id==sayi) {
				System.out.println(resourceList.get(i)+ " verim " + ((double)(resourceList.get(i).totalTime)/(resourceList.get(i).endedTimes.get(resourceList.get(i).endedTimes.size()-1)+1)));
				}
			}
		}
	
	public void run() {
		
		/* 	WAITLIST DOGRU SORT EDILMIS DIYE KONTROL EDILMEK UZERE EKLENDI ISTENILIRSE ACILABIILIR
		System.out.println("Sorted waitlist before adding:  " + waitList);
		*/
		
		
		//	Burasi waitList'ten cekilen job'larin uygun queue'lara atilmasini kontrol eder.
		int firstNumber = 0;
		while(!waitList.isEmpty()) {
			while((!waitList.isEmpty())&&(waitList.first().arrivalTime==firstNumber)) {
			if((waitList.first().arrivalTime==firstNumber)&&(waitList.first().priority.equals("H"))){
				highPQueue.push(waitList.removeFirst());	
			}
			else if((waitList.first().arrivalTime==firstNumber)&&(waitList.first().priority.equals("M"))){
				midPQueue.push(waitList.removeFirst());
			}
			else if((waitList.first().arrivalTime==firstNumber)&&(waitList.first().priority.equals("L"))){
				lowPQueue.push(waitList.removeFirst());
			}
			}
			firstNumber++;
		}
		
		
		/* QUEUE'LAR DOGRU MU DIYE TEST EDILMEK UZERE KONDU ISTERSE ACILABILIR

		System.out.println("----------------------------------------------");
		System.out.println("HIGH QUEUE: " + highPQueue);
		System.out.println("MID  QUEUE: " + midPQueue );
		System.out.println("LOW  QUEUE: " + lowPQueue );
		System.out.println("Waitlist  : " + waitList  );
		System.out.println("----------------------------------------------");
		System.out.println();
		
		*/
		
		int toplamDeger=0;
		System.out.print("Zaman");
		for(int i=0;i<resourceList.size();i++)
		System.out.print("\t"+resourceList.get(i));
		System.out.println();
		
		while(true){
			addingJobs();	// job'lar queue'lardan cekilip resource'lara eklenir.
			
			//	Her asamayi print eden kisim asagida
			System.out.print(zaman);
			for(int i=0; i<resourceList.size();i++) {
				if(resourceList.get(i).isEmpty()) {
					System.out.print("\t");
				}
				if(!resourceList.get(i).isEmpty()) {
				System.out.print("\tJ" + resourceList.get(i).jobs.get(0).id);
				}
			}
			System.out.println();
			
			
			//	Eger job'un duration'u bittiyse burada job kill edilir ve bitis listesine eklenir.
			for(int i=0;i<resourceList.size();i++) {
				resourceList.get(i).start();
				if(resourceList.get(i).durationTime==0) {
					if(!resourceList.get(i).isEmpty()) {
						resourceList.get(i).endedTimes.add(zaman);
						resourceList.get(i).killJob();
					}
				}
			}
			zaman++;
			
			//	Burasi cikis yapip yapmamayi kontrol eder. Eger tum resource'lar bossa break edilir.
			if(waitList.isEmpty()&&highPQueue.isEmpty()&&midPQueue.isEmpty()&&lowPQueue.isEmpty()){
				toplamDeger=0;
				for(int i=0;i<resourceList.size();i++) {
					if(resourceList.get(i).isEmpty()) {
						toplamDeger++;
					}
				}
				if(resourceList.size()==toplamDeger) {
					break;
				}
			}
			
		}
	}
	public void resourceExplorer(int sayi) {
		
		/* Burada resourceList'ten aranan indexteki resource cekilir ve buradaki gecmis job'lar cikti olarak verilir,
		 * gecmis job'lar resource'in icinde bulunan oldJobs arrayinde tutulur.
		 */
		
		for(int i=0;i<resourceList.size();i++) {
			if(sayi==resourceList.get(i).id) {
				System.out.print(resourceList.get(i)+" ");
				for(int j=0;j<resourceList.get(i).oldJobs.size();j++) {
					System.out.print("("+resourceList.get(i).oldJobs.get(j).id+", "+resourceList.get(i).endedTimes.get(j) + ", " +(resourceList.get(i).startedTimes.get(j) - resourceList.get(i).arrivalTimes.get(j))+") ");
				}
				System.out.println();
				break;
			}
		}
	}
	
	public void addingJobs() {
		
		//	Burada resource'lar bos oldugu takdirde high'dan baslanarak low'a kadar atama yapilir.
		
		for(int i=0;i<resourceList.size();i++) {
			if(resourceList.get(i).isEmpty()) {
				if(!highPQueue.isEmpty()&&resourceList.get(i).isEmpty()&&highPQueue.top().arrivalTime<=zaman){
					if(highPQueue.top().arrivalTime<=zaman) {
						resourceList.get(i).arrivalTimes.add(highPQueue.top().arrivalTime);
						resourceList.get(i).add(highPQueue.pop());
						resourceList.get(i).startedTimes.add(zaman);
					}
				}
				else if(!midPQueue.isEmpty()&&resourceList.get(i).isEmpty()&&midPQueue.top().arrivalTime<=zaman) {
					if(midPQueue.top().arrivalTime<=zaman) {
						resourceList.get(i).arrivalTimes.add(midPQueue.top().arrivalTime);
						resourceList.get(i).add(midPQueue.pop());
						resourceList.get(i).startedTimes.add(zaman);
					}
				}
				else if(!lowPQueue.isEmpty()&&resourceList.get(i).isEmpty()&&lowPQueue.top().arrivalTime<=zaman) {
					if(lowPQueue.top().arrivalTime<=zaman) {
						resourceList.get(i).arrivalTimes.add(lowPQueue.top().arrivalTime);
						resourceList.get(i).add(lowPQueue.pop());
						resourceList.get(i).startedTimes.add(zaman);
					}
				}
			}
		}
	}
	public void jobExplorer(job j) {

		/*	Aranan job'un oldugu resource kontrol edilir ve basladigi zaman, 
		 * bittigi zaman ve arrivalTime'indan kaynakli gecikme resource icinde bulunan listelerden alinir.
		*/
		
		System.out.println("islemno\tkaynak\tbaslangic\tbitis\tgecikme");
		int i=0;
		int a=0;
		for(i=0;i<resourceList.size();i++) {
			for(int k=0;k<resourceList.get(i).oldJobs.size();k++) {
				if(resourceList.get(i).oldJobs.get(k).id==j.id) {
					a=resourceList.get(i).oldJobs.indexOf(j);
					System.out.println(j.id+"\tR"+(i+1) + "\t"+(resourceList.get(i).startedTimes.get(a)+ "\t\t"+(resourceList.get(i).endedTimes.get(a)+ "\t" +(resourceList.get(i).startedTimes.get(a) - resourceList.get(i).arrivalTimes.get(a)))));
					break;
			}

		}
	}
		}
}

class SinglyLinkedList{
    private static class Node{
        private job element;
        private Node next;
        public Node(job j, Node n){
            this.element=j;
            this.next=n;
        }
        public job getElement() {
            return element;
        }
        public Node getNext(){
            return next;
        }
        public void setNext(Node n){
            next=n;
        }
    }
    private Node head=null;
    private Node tail=null;
    private int size=0;

    public SinglyLinkedList() {
    }
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public job first(){
        if(isEmpty())
            return null;
        return head.getElement();
    }
    public void addFirst(job j) {
        head = new Node(j,head);
        if(size==0)
            tail=head;
        size++;
    }
    public void bubbleSort() {
    	
    	//	211'de implement ettigim bubbleSort'u burada kullandim.
        if (size>1) {
            boolean wasChanged;
            do {
                Node current = head;
                Node previous = null;
                Node next = head.next;
                wasChanged = false;

                while ( next != null ) {
                    if (current.getElement().arrivalTime>next.getElement().arrivalTime) {
                        wasChanged = true;
                        if ( previous != null ) {
                            Node sig = next.next;

                            previous.next = next;
                            next.next = current;
                            current.next = sig;
                        } else {
                            Node sig = next.next;
                            head = next;
                            next.next = current;
                            current.next = sig;
                        }

                        previous = next;
                        next = current.next;
                    } else { 
                        previous = current;
                        current = next;
                        next = next.next;
                    }
                } 
            } while(wasChanged);
        }
    }
    
    public void add(job j) {
        Node node = new Node(j,null);
        if (head == null) {
            head = node;
        } else {
            Node currentNode = head;
            while(currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
        size++;     
    }
    
    public job removeFirst(){
        if(isEmpty())
            return null;
        job answer = head.getElement();
        if(head!=tail) {
        	 head = head.getNext();
        }
        else {
        	head=null;
        	tail=null;
        }
        size--;
        if(size==0)
            tail=null;
        return answer;
    }
    
    public String toString() {
    	//	LinkedList'leri bastirmak icin kendi StringBuilder'imi buraya ekledim. Aksi taktirde adres basiyordu.
 	   StringBuilder sb = new StringBuilder("");
 	   Node walk = head;
		if(walk==null) {
			return "is empty.";
		}
 	   while (walk != null) {
 	     sb.append("("+walk.getElement().arrivalTime + "," + walk.getElement().id + "," + walk.getElement().priority + "," + walk.getElement().duration +")");
 	     if (walk != null)
 	       sb.append(" ");
 	     walk = walk.getNext();
 	   }
 	   sb.deleteCharAt(sb.length()-1);
 	   return sb.toString();
 	 }
}

class Queue{
	public SinglyLinkedList liste;
	public Queue(){
		liste = new SinglyLinkedList();	
	}
	public boolean isEmpty() {
		return liste.size()==0;
	}
	public job top() {
		return liste.first();
	}
	public void push(job j) {
		liste.add(j);
	}
	public job pop() {
		return liste.removeFirst();
	}
	public String toString() {
		return liste.toString();
	}
 }

class Resource{
	int id;
	int durationTime;
	int totalTime;
	
	//	Odev metninde anlasildigi uzere asagida olusturulanlar bagli liste yapisinda olusturulmus waitList ve queue'lardan farkli olarak Resource icin eklenmistir.
	
	ArrayList<job> jobs = new ArrayList<>();	//	Yapilmak uzere gelen job'u tutar bittigi zaman silinir.
	ArrayList<job> oldJobs = new ArrayList<>();		//	Bitenler dahil tum yapilan job'lari tutar.
	ArrayList<Integer> arrivalTimes = new ArrayList<>();	//	Resource'a eklenen job'larin arrivalTime'larini sirayla tutar.
	ArrayList<Integer> startedTimes = new ArrayList<>();	//	Resource'a eklenen job'larin baslangic surelerini sirayla tutar.
	ArrayList<Integer> endedTimes = new ArrayList<>();	//	Resource'a eklenen job'larin bitis surelerini sirayla tutar.
	
	public Resource(int i) {
		//	Burasi tum zaman birimleri olan bir resource olusturur.
		this.id=i;
		this.durationTime=0;
		this.totalTime=0;
	}
	public String toString() {
		return "R"+this.id;
	}
	public void add(job j) {
		if(durationTime==0) {
			oldJobs.add(j);		//	Verinin kaybolmamasi icin ayni zamanda history olarak tutulan oldJobs'a da eklenir.
			jobs.add(j);
		}
		this.durationTime+=j.duration;
		this.totalTime+=j.duration;
	}

	public void start() {
		//	Burasi her bir zaman araliginda job'larin durationTime'ini azaltir.
		if(durationTime>0) {
			durationTime--;
		}
	}
	public boolean isEmpty() {
		return (jobs.size()==0);
	}
	public void killJob() {
		//	durationTime bittiginde buraya girer ve job'u remove eder.
		jobs.remove(0);
	}
	
}
