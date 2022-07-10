package com.ritesh.testing.repository;

import com.ritesh.testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository
    extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

  Optional<Employee> findByEmail(String email);

  @Query("from Employee where firstName=?1 and lastName=?2")
  Employee findByFirstNameAndLastName(String firstName, String lastName);

  @Query("from Employee where firstName=:firstName and lastName=:lastName")
  Employee findByCustomQueryWithNamedParams(
      @Param("firstName") String firstName, @Param("lastName") String lastName);

  @Query(value = "select * from employee where first_name=?1 and last_name=?2", nativeQuery = true)
  Employee findByFirstNameAndLastNameWithNativeQuery(String firstName, String lastName);

  @Query(
      value = "select * from employee where first_name=:firstName and last_name=:lastName",
      nativeQuery = true)
  Employee findByFirstNameAndLastNameWithNativeQueryParams(
      @Param("firstName") String firstName, @Param("lastName") String lastName);
}
