package com.pm.user_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "users_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "username")
    private String username;

    @Column(name = "fullname")
    private String fullname;

    private Integer age;
    private String gender; // MALE, FEMALE, OTHER
    private Double height; // in cm
    private Double weight; // in kg
    private String fitnessGoal; // e.g., "Weight Loss", "Muscle Gain"
    private String activityLevel; // e.g., "Sedentary", "Active"

    private List<String> preferredWorkouts; // e.g., ["Cardio", "Yoga", "HIIT"]
    private List<UUID> enrolledPrograms; // references to WorkoutProgram collection
    private List<UUID> completedWorkouts; // history of completed workouts

    private Integer points = 0;
    private List<String> achievements; // e.g., "7-day Streak", "100 Workouts"

    @CreatedDate
    @Column(name = "creatiion_timestamp")
    private Date creationTimestamp;

    @LastModifiedDate
    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimestamp;
}
