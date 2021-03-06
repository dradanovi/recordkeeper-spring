package hr.java.web.radanovic.recordkeeper.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.java.web.radanovic.recordkeeper.dto.FilerHoursRequest;
import hr.java.web.radanovic.recordkeeper.dto.StartSubjectRequest;
import hr.java.web.radanovic.recordkeeper.dto.WorkHoursDto;
import hr.java.web.radanovic.recordkeeper.dto.WorkSubjectDto;
import hr.java.web.radanovic.recordkeeper.service.WorkHoursService;
import hr.java.web.radanovic.recordkeeper.service.WorkSubjectsService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/work")
@AllArgsConstructor
public class WorkController {

	private final WorkHoursService hoursService;
	private final WorkSubjectsService subjectService;

	@PostMapping("/hours/new")
	public ResponseEntity<String> startHours() {
		hoursService.startHours();
		return new ResponseEntity<String>("Hours Started", HttpStatus.CREATED);
	}
	
	@GetMapping("/hours/check")
	public ResponseEntity<String> checkHours(){
		return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(hoursService.checkHours()));
	}
	
	@GetMapping("/hours/check2")
	public ResponseEntity<List<WorkHoursDto>> ch() {
		return ResponseEntity.status(HttpStatus.OK).body(hoursService.test());
	}
	
	
	@GetMapping("/subject/check")
	public ResponseEntity<String> checkSubject(){
		return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(subjectService.checkSubject()));
	}

	@PostMapping("/subject/new")
	public ResponseEntity<String> startSubject(@RequestBody StartSubjectRequest subjectDto) {
		subjectService.startSubject(subjectDto);
		return new ResponseEntity<String>("Subject Created", HttpStatus.OK);
	}

	@PostMapping("/hours/close")
	public ResponseEntity<String> endHours() {
		hoursService.endHours();
		return new ResponseEntity<String>("Hours Finished", HttpStatus.CREATED);
	}

	@PostMapping("/subject/close")
	public ResponseEntity<String> endSubject() {
		subjectService.endSubject();
		return new ResponseEntity<String>("Subject Ended", HttpStatus.OK);
	}

	@PostMapping("/hours/display")
	public ResponseEntity<List<WorkHoursDto>> displayHours(@RequestBody FilerHoursRequest hoursDto) {
		Long start, end, jpa, scr;
		start = System.nanoTime();
		hoursService.getHoursDisplayFilter(hoursDto);
		end = System.nanoTime();
		jpa = end - start;
		System.out.println("jpa time -> " +  jpa);
		start = System.nanoTime();
		hoursService.getHoursDisplayScript(hoursDto);
		end = System.nanoTime();
		scr = end - start;
		System.out.println("scr time -> " +  scr);
		System.out.println("ratio -> " + jpa / scr);
		return ResponseEntity.status(HttpStatus.OK).body(hoursService.getHoursDisplayScript(hoursDto));
	}

	@PostMapping("/subject/display")
	public ResponseEntity<List<WorkSubjectDto>> displaySubjects(@RequestBody FilerHoursRequest subjectsDto) {
		return ResponseEntity.status(HttpStatus.OK).body(subjectService.getSubjectsDisplayFilter(subjectsDto));
	}
	
	@PostMapping("/hours/total")
	public ResponseEntity<String> hoursTotal(@RequestBody List<WorkHoursDto> listHours){
		return new ResponseEntity<String>(hoursService.hoursWorked(listHours), HttpStatus.OK);
	}
}
