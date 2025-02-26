package com.jobportal.jobportal.repository;

import com.jobportal.jobportal.entity.IRecruiterJobs;
import com.jobportal.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity,Integer> {

    @Query(value = "SELECT COUNT(s.user_id) AS totalCandidates, j.job_post_id AS jobPostId, " +
            "j.job_title AS jobTitle," +
            "l.id AS locationId, l.country, l.city, l.state," +
            "c.id AS companyId, c.name FROM job_post_activity j JOIN job_location l " +
            "ON j.job_location_id = l.id " +
            "JOIN job_company c " +
            "ON j.job_company_id = c.id " +
            "LEFT JOIN job_seeker_apply s " +
            "ON s.job = j.job_post_id " +
            "WHERE j.posted_by_id = :recruiter " +
            "GROUP BY j.job_post_id", nativeQuery = true)
    List<IRecruiterJobs> getRecruiterJobs(@Param("recruiter") int recruiter);

    @Query(value = "SELECT * FROM job_post_activity j JOIN job_location l " +
            "ON j.job_location_id = l.id " +
            "WHERE j.job_title LIKE %:job% " +
            "AND (l.country LIKE %:location% " +
            "OR l.city LIKE %:location% " +
            "OR l.state LIKE %:location%) " +
            "AND j.job_type IN :type " +
            "AND j.remote IN :remote " +
            "AND (j.posted_date >= :date)", nativeQuery = true)
    List<JobPostActivity> search(@Param("job") String job,
                                 @Param("location") String location,
                                 @Param("type") List<String> type,
                                 @Param("remote") List<String> remote,
                                 @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM job_post_activity j JOIN job_location l " +
            "ON j.job_location_id = l.id " +
            "WHERE j.job_title LIKE %:job% " +
            "AND (l.country LIKE %:location% " +
            "OR l.city LIKE %:location% " +
            "OR l.state LIKE %:location%) " +
            "AND j.job_type IN %:type% " +
            "AND j.remote IN %:remote%", nativeQuery = true)
    List<JobPostActivity> searchWithoutDate(@Param("job") String job,
                                            @Param("location") String location,
                                            @Param("type") List<String> type,
                                            @Param("remote") List<String> remote);
}
