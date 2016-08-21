package Day2; 
import java.util.List;
 import org.hibernate.Query; 
 import org.hibernate.Session; 
 import org.hibernate.SessionFactory; 
 import org.hibernate.Transaction; 
 import org.hibernate.cfg.Configuration; 
 import org.hibernate.service.ServiceRegistry; 
 import org.hibernate.service.ServiceRegistryBuilder; 

 public class TestAssignment { 
 	SessionFactory factory; 
 	 
 	public void setup(){ 
 		Configuration configuration = new Configuration(); 
 		configuration.configure(); 
 		ServiceRegistryBuilder srBuilder = new ServiceRegistryBuilder(); 
 		srBuilder.applySettings(configuration.getProperties()); 
 		ServiceRegistry serviceRegistry = srBuilder.buildServiceRegistry(); 
 		factory = configuration.buildSessionFactory(serviceRegistry); 
 	} 
 	 
 	 
 	public Skill findSkill(Session session, String name){ 
 		Query query = session.createQuery("from Skill s where s.name=:name"); 
 		query.setParameter("name", name); 
 		Skill skill = (Skill) query.uniqueResult(); 
 		return skill; 
 	} 
 	 
 	public Skill saveSkill(Session session, String name){ 
 		Skill skill = findSkill(session,name);  
 		if (skill == null){ 
 			skill = new Skill(); 
 			skill.setName(name); 
 			session.save(skill); 
 		} 
 		return skill; 
 	} 
 	 
 	public Student findStudent(Session session, String name){ 
 		Query query = session.createQuery("from Student s where s.name=:name"); 
 		query.setParameter("name", name); 
 		Student student = (Student) query.uniqueResult(); 
 		return student; 
 	} 
 	 
 	public Student saveStudent(Session session, String name){ 
 		Student student = findStudent(session,name);  
 		if (student == null){ 
 			student = new Student(); 
 			student.setName(name); 
 			session.save(student); 
 		} 
 		return student; 
 	} 
 	public void createData(Session session, String subjectName, String observerName, String skillName, int rank){
 		Student subject = saveStudent(session,subjectName);
 		Student observer = saveStudent(session,observerName);
 		Skill skill = saveSkill(session,skillName);
 		Ranking ranking = new Ranking();
 		ranking.setSubject(subject);
 		ranking.setObserver(observer);
 		ranking.setSkill(skill);
 		ranking.setRating(rank);
 		session.save(ranking);
 		}
 	
 	     // Changing Rank
 	
 		public void changeRank(Session session, String subjectName, String observerName, String skillName, int newRating){
 		Query query = session.createQuery("from Ranking r "
 		+ "where r.subject.name=:subject and "
 		+ "r.observer.name=:observer and "
 		+ "r.skill.name=:skill");
 		query.setString("subject", subjectName);
 		query.setString("observer",observerName);
 		query.setString("skill", skillName);
 		Ranking ranking = (Ranking) query.uniqueResult();
 		if(ranking == null){
 		System.out.println("Invalid Change Request");
 		}
 		ranking.setRating(newRating);
 		}
 		
 		//Removing rank given to a person on particular skill
 		
 		public void RemoveRank(Session session, String subjectName, String observerName, String skillName){
 	 		Query query = session.createQuery("from Ranking r "
 	 		+ "where r.subject.name=:subject and "
 	 		+ "r.observer.name=:observer and "
 	 		+ "r.skill.name=:skill");
 	 		query.setString("subject", subjectName);
 	 		query.setString("observer",observerName);
 	 		query.setString("skill", skillName);
 	 		Ranking ranking = (Ranking) query.uniqueResult();
 	 		if(ranking==null){
 	 		System.out.println("Invalid Remove Request");
 	 		}
 	 		else{
 	 		session.delete(ranking);
 	 		System.out.println("Sucessfully removed rank of "+subjectName);
 	 	     	}
 		}
 		
 		//Getting average based on skill with error handling
 		
 		public void GetAverage(Session session,String subjectName,String skillName){
 		 			Query query = session.createQuery("Select avg(r.rating) from Ranking r "
 	 		+ "where r.subject.name=:subject and "
 	 		+ "r.skill.name=:skill");
 			query.setString("subject", subjectName);
 	 		query.setString("skill", skillName);
 	 	    List<Integer> average=(List<Integer>) query.list();
 	 	    if(average.get(0)==null){
 	 	    		System.out.println(subjectName+" dont have any rating on "+skillName);
 	 	    }
 	 	    else{
 	 	    System.out.println("Average of "+subjectName+" on the skill "+skillName+" is "+average.get(0));
 		}
 		}
 	 
 		//Finding Topper with error handling
 		
 		public void Topper(Session session,String skillName){
	 			Query query = session.createQuery("Select r.subject from Ranking r where r.skill.name=:skill and r.rating=(Select max(ra.rating) from Ranking ra where ra.skill.name=:skill )");
		query.setString("skill", skillName);
	    List<Student> topper=(List<Student>) query.list();
	    if(topper.size()==0){
	    	System.out.println("No one is there with "+skillName+" skill");
	    }
	    else{
	    System.out.println("Topper for the skill "+skillName+" is "+topper.get(0).getName());
	        }
 		}
 		
 		//Sorting students on particular skill
 		public void sortStudents(Session session,String skillName){
 			Query query = session.createQuery("Select DISTINCT(r.subject.name) from Ranking r where r.skill.name=:skill order by(r.subject.name)");
	            query.setString("skill", skillName);
            List<String> srt=(List<String>) query.list();
    if(srt.size()==0){
    	System.out.println("No one is there with "+skillName+" skill");
    }
    else{
    	for(String s:srt){
    System.out.println(s);
        }
    }
		}
	public static void main(String[] args) { 
 		// TODO Auto-generated method stub 
		 
 		TestAssignment tp = new TestAssignment(); 
 		tp.setup(); 
 		
 		 
 		Session session = tp.factory.openSession(); 
 		Transaction tx = session.beginTransaction(); 
		
 		/*
 		 Student s1 = tp.saveStudent(session, "Awantik Das"); 
 		Student s2 = tp.saveStudent(session, "Jack Sparrow"); 
		 
 		Skill skill = tp.saveSkill(session, "Hibernate"); 
 		 
 		Ranking ranking = new Ranking(); 
 		ranking.setSubject(s1); 
 		ranking.setObserver(s2); 
 		ranking.setSkill(skill); 
 		ranking.setRating(7); 
 		 
 		session.save(ranking);*/
 
 		 //Add ranks
 	/*	 tp.createData(session, "Amit","Vijay","Python",5);
 		 tp.createData(session, "Ajit","Nilesh","Django",9);
 		tp.createData(session, "Amway","Dash","Spring",8);
 		
 		
 		tx.commit();
 		session.close();*/
 		
 	// UPDATING RATING assigned by Jack Sparrow to Awantik Das
 		// tp.changeRank(session, "Awantik Das","Jack Sparrow","Hibernate",12); 		
 		
 		/*5th Adding rank to amit on python skill bye diff observers to test GetAverage method
 		tp.createData(session, "Amit","Nilesh","Python",12);
		 tp.createData(session, "Amit","Dash","Python",9);
		tp.createData(session, "Amit","Amway","Python",8);*/
 		
 		/*tp.createData(session, "Dash","Ajit","Python",14);
 		tp.createData(session, "Nilesh","Amway","Python",7);
 		tp.createData(session, "Amway","Awantik","Python",8);*/
 		
 		//REMOVING RANK (Both success and failure case tested) 
 		//tp.RemoveRank(session,"Awantik Das","Jack Sparrow","Hibernate");
 		//tp.RemoveRank(session,"Amit","Vijay","Python");

 		
 		//GETTING AVERAGE
 		//tp.GetAverage(session,"Amit","Python"); //(Success case tested)
 		//tp.GetAverage(session,"Ajit","Python"); //(Failure case tested)
 		
 		//FINDING TOPPER
 		//tp.Topper(session,"Python");  //(Success case tested)
 		//tp.Topper(session,"Hibernate");  //(Failure case tested)
 		
 		//SORTING STUDENTS
 		//tp.sortStudents(session,"Python");   //(sucess case tested)
 		//tp.sortStudents(session,"Hibernate");  // (Failure case tested)
 	
 		tx.commit();
 	    session.close();
 	
 	} 
 
 } 
