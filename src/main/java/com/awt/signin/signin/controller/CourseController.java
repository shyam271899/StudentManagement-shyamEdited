package com.awt.signin.signin.controller;

import com.awt.signin.signin.entity.Courses;
import com.awt.signin.signin.entity.Section;
import com.awt.signin.signin.entity.SubSection;
import com.awt.signin.signin.repository.CoursesRepository;
import com.awt.signin.signin.repository.SectionRepository;
import com.awt.signin.signin.repository.SubSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    @Autowired
    private CoursesRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubSectionRepository subSectionRepository;


    @GetMapping("/courses")
    public List<Courses> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/sections")
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @GetMapping("/subsections")
    public List<SubSection> getAllSubSections() {
        return subSectionRepository.findAll();
    }


    @PostMapping("/courses")
    public ResponseEntity<String> createCourses(@RequestBody List<Courses> coursesList) {
        List<Courses> savedCourses = new ArrayList<>();
        for (Courses courses : coursesList) {
            Optional<Courses> existingCourse = courseRepository.findByCourseName(courses.getCourseName());

            if (existingCourse.isPresent()) {
                Courses existingCourseEntity = existingCourse.get();
                existingCourseEntity.setSections(courses.getSections());
                courses = existingCourseEntity;
            }


            List<Section> sections = courses.getSections();
            if (sections != null) {
                for (Section section : sections) {
                    Optional<Section> existingSection = sectionRepository.findBySectionName(section.getSectionName());

                    if (existingSection.isPresent()) {
                        Section existingSectionEntity = existingSection.get();
                        existingSectionEntity.setPercentage(section.getPercentage());
                        existingSectionEntity.setSubSections(section.getSubSections());
                        section.setSection_id(existingSectionEntity.getSection_id());
                        section.setCourses(existingSectionEntity.getCourses());

                    } else {
                        section.setCourses(courses);
                    }

                    List<SubSection> subSections = section.getSubSections();
                    if (subSections != null) {
                        for (SubSection subSection : subSections) {
                            Optional<SubSection> existingSubSection = subSectionRepository.findBySubsectionName(subSection.getSubsectionName());

                            if (existingSubSection.isPresent()) {
                                SubSection existingSubSectionEntity = existingSubSection.get();
                                existingSubSectionEntity.setReferenceLinks(subSection.getReferenceLinks());
                                subSection.setSubSectionId(existingSubSectionEntity.getSubSectionId());
                                subSection.setSection(existingSubSectionEntity.getSection());
                                subSection.setCourses(existingSubSectionEntity.getCourses());
                            } else {
                                subSection.setSection(section);
                                subSection.setCourses(courses);
                            }
                        }
                    }
                }
            }

            savedCourses.add(courses);
        }
        courseRepository.saveAll(savedCourses);

        return ResponseEntity.status(HttpStatus.CREATED).body("Saved Courses Successfully");
    }




    @DeleteMapping("/courses/{course_id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int course_id) {
        Optional<Courses> courseOptional = courseRepository.findById(course_id);
        if (courseOptional.isPresent()) {
            Courses course = courseOptional.get();
            List<Section> sections = course.getSections();
            if (sections != null) {
                for (Section section : sections) {
                    List<SubSection> subSections = section.getSubSections();
                    if (subSections != null) {
                        for (SubSection subSection : subSections) {
                            subSection.getCourses();
                        }
                    }
                    section.getSubSections();
                }
            }

            course.getSections();
            return ResponseEntity.ok("Course and its associated sections and subsections retrieved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("Course with ID" + course_id + "not found");
        }
    }


    @GetMapping("/courses/{course_id}")
    public ResponseEntity<Courses> getCourseWithSectionsAndSubsections(@PathVariable int course_id) {
        Optional<Courses> optionalCourse = courseRepository.findById(course_id);
        if (optionalCourse.isPresent()) {
            Courses course = optionalCourse.get();


            List<Section> sections = sectionRepository.findByCourses(course);
            for (Section section : sections) {
                List<SubSection> subSections = subSectionRepository.findBySection(section);
                section.setSubSections(subSections);
            }
            course.setSections(sections);

            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/sections/{section_id}")
    public ResponseEntity<Section> getSectionWithSubsections(@PathVariable int section_id) {
        Optional<Section> optionalSection = sectionRepository.findById(section_id);
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();

            List<SubSection> subSections = subSectionRepository.findBySection(section);
            section.setSubSections(subSections);

            return ResponseEntity.ok(section);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/subsections/{subsection_id}")
    public ResponseEntity<SubSection> getSubsections(@PathVariable int subsection_id) {
        Optional<SubSection> optionalSubSection = subSectionRepository.findById(subsection_id);
        if (optionalSubSection.isPresent()) {
            SubSection subSection = optionalSubSection.get();

            return ResponseEntity.ok(subSection);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}