package de.devroyal.webapp.timesheet;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.devroyal.webapp.timesheet.util.HibernateUtil;

@Path("services/timesheet")
public class TimesheetServiceProvider {

	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the client as "text/plain" media type.
	 * 
	 * @return String that will be returned as a text/plain response.
	 */

	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<WorkingDay> get() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<WorkingDay> workingDays = null;
		try {
			tx = session.beginTransaction();
			workingDays = session.createQuery("FROM WorkingDay").list();

			// for (Iterator iterator = e.iterator(); iterator.hasNext();) {
			// Worker worker = (Worker) iterator.next();
			// System.out.print("First Name: " + worker.getFirstname());
			// System.out.print(" Last Name: " + worker.getLastname());
			// System.out.println(" Salary: " + worker.getSalary());
			// }
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		System.out.println("Successful run");
		return workingDays;
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/get/{wID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public WorkingDay getWorkingDay(@PathParam("wID") int wID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		WorkingDay wDay = null;
		try {
			tx = session.beginTransaction();
			wDay = (WorkingDay) session.get(WorkingDay.class, wID);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		System.out.println("Successful run");
		return wDay;
	}

	@PUT
	@Path("/create")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createWorkingDay(JAXBElement<WorkingDay> workingDay) {
		System.out.println("REST Service Method createWorkingDay called with: " + workingDay);

		WorkingDay wDay = workingDay.getValue();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Integer workerID = null;
		try {
			tx = session.beginTransaction();

			workerID = (Integer) session.save(wDay);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return Response.created(uriInfo.getAbsolutePath()).build();
	}

	@PUT
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateWorkingDay(JAXBElement<WorkingDay> workingDay) {
		System.out.println("REST Service Method updateWorkingDay called");

		WorkingDay wDay = workingDay.getValue();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Integer workerID = null;
		try {
			tx = session.beginTransaction();

			session.update(wDay);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return Response.created(uriInfo.getAbsolutePath()).build();
	}

	@DELETE
	@Path("/delete/{wID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public void deleteWorkingDays(@PathParam("wID") int wID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		WorkingDay wDay = null;
		try {
			tx = session.beginTransaction();
			wDay = (WorkingDay) session.get(WorkingDay.class, wID);
			session.delete(wDay);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		System.out.println("Successful run");

	}
	
	

	
}
