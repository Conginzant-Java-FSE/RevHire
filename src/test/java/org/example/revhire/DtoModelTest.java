package org.example.revhire;

import org.example.revhire.enums.ApplicationStatus;
import org.example.revhire.enums.Role;
import org.example.revhire.model.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;

public class DtoModelTest {

    private int dummyCounter = 0;

    @Test
    public void testAllDtosAndModels() throws Exception {
        List<String> packages = Arrays.asList(
                "org.example.revhire.dto.request",
                "org.example.revhire.dto.response",
                "org.example.revhire.model");

        for (String pkg : packages) {
            List<Class<?>> classes = getClasses(pkg);
            for (Class<?> clazz : classes) {
                if (clazz.isEnum() || clazz.isInterface() || clazz.isAnonymousClass())
                    continue;
                testBoilerplate(clazz);
            }
        }
    }

    private void testBoilerplate(Class<?> clazz) throws Exception {
        Object instance1 = tryCreateInstance(clazz);
        Object instance2 = tryCreateInstance(clazz);

        if (instance1 == null)
            return;
        List<Method> setters = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                setters.add(method);
                String getterName = "g" + method.getName().substring(1);
                String isGetterName = "is" + method.getName().substring(3);

                Method getter = null;
                try {
                    getter = clazz.getMethod(getterName);
                } catch (Exception e) {
                }
                if (getter == null) {
                    try {
                        getter = clazz.getMethod(isGetterName);
                    } catch (Exception e) {
                    }
                }

                if (getter != null) {
                    Object value = createDummyValue(method.getParameterTypes()[0]);
                    if (value != null) {
                        method.invoke(instance1, value);
                        if (instance2 != null) {
                            method.invoke(instance2, value);
                        }
                        assertEquals(value, getter.invoke(instance1),
                                "Getter/Setter failed for " + clazz.getSimpleName() + "." + getter.getName());
                    }
                }
            }
        }

        String str = instance1.toString();
        assertNotNull(str, "toString() returned null for " + clazz.getSimpleName());

        assertEquals(instance1, instance1);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, new Object());

        if (instance2 != null) {
            try {
                Method equalsMethod = clazz.getMethod("equals", Object.class);
                if (equalsMethod.getDeclaringClass() == clazz) {
                    assertEquals(instance1, instance2,
                            "equals() failed for two identically populated " + clazz.getSimpleName());
                    assertEquals(instance1.hashCode(), instance2.hashCode(),
                            "hashCode() mismatch for equal " + clazz.getSimpleName());
                }
            } catch (Exception e) {

            }

            if (!setters.isEmpty()) {
                dummyCounter = 100;
                for (Method setter : setters) {
                    try {
                        Object differentValue = createDummyValue(setter.getParameterTypes()[0]);
                        if (differentValue != null) {
                            setter.invoke(instance2, differentValue);
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
                instance1.equals(instance2);
                instance2.hashCode();
                dummyCounter = 0;
            }
        }
    }

    @Test
    public void testAllArgsConstructors() throws Exception {
        List<String> packages = Arrays.asList(
                "org.example.revhire.dto.request",
                "org.example.revhire.dto.response",
                "org.example.revhire.model");

        for (String pkg : packages) {
            List<Class<?>> classes = getClasses(pkg);
            for (Class<?> clazz : classes) {
                if (clazz.isEnum() || clazz.isInterface() || clazz.isAnonymousClass())
                    continue;
                for (java.lang.reflect.Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() > 0) {
                        constructor.setAccessible(true);
                        Object[] args = new Object[constructor.getParameterCount()];
                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        for (int i = 0; i < paramTypes.length; i++) {
                            args[i] = createDummyValue(paramTypes[i]);
                        }
                        try {
                            Object instance = constructor.newInstance(args);
                            assertNotNull(instance, "All-args constructor failed for " + clazz.getSimpleName());
                        } catch (Exception e) {
                            // Skip if constructor has dependencies we can't fulfill
                        }
                    }
                }
            }
        }
    }

    private Object tryCreateInstance(Class<?> clazz) {
        try {
            java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private Object createDummyValue(Class<?> type) {
        if (type == String.class)
            return "test" + dummyCounter;
        if (type == Integer.class || type == int.class)
            return 1 + dummyCounter;
        if (type == Long.class || type == long.class)
            return 1L + dummyCounter;
        if (type == Double.class || type == double.class)
            return 1.0 + dummyCounter;
        if (type == Boolean.class || type == boolean.class)
            return dummyCounter % 2 == 0;
        if (type == List.class)
            return new ArrayList<>();
        if (type == java.time.LocalDate.class)
            return java.time.LocalDate.now().plusDays(dummyCounter);
        if (type == java.time.LocalDateTime.class)
            return java.time.LocalDateTime.now().plusDays(dummyCounter);
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants[dummyCounter % constants.length];
        }
        if (type == byte[].class)
            return new byte[] { 1, 2, 3 };
        try {
            java.lang.reflect.Constructor<?> noArgCtor = type.getDeclaredConstructor();
            noArgCtor.setAccessible(true);
            return noArgCtor.newInstance();
        } catch (Exception e) {

        }
        return null;
    }

    private List<Class<?>> getClasses(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private List<Class<?>> findClasses(File directory, String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    @Test
    public void testBaseEntityGettersSetters() {
        Job job = new Job();
        LocalDateTime now = LocalDateTime.now();

        job.setCreatedAt(now);
        assertEquals(now, job.getCreatedAt());
        job.setUpdatedAt(now);
        assertEquals(now, job.getUpdatedAt());
        job.setCreatedBy("admin");
        assertEquals("admin", job.getCreatedBy());
        job.setUpdatedBy("admin");
        assertEquals("admin", job.getUpdatedBy());
    }

    @Test
    public void testUserAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                1L, "Test", "test@test.com", "pass",
                Role.EMPLOYER, "1234567890", "NYC", "What?", "Answer", true, now);

        assertEquals(1L, user.getId());
        assertEquals("Test", user.getName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(Role.EMPLOYER, user.getRole());
        assertEquals("1234567890", user.getPhone());
        assertEquals("NYC", user.getLocation());
        assertEquals("What?", user.getSecurityQuestion());
        assertEquals("Answer", user.getSecurityAnswer());
        assertTrue(user.isActive());
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    public void testEmployerStatsGettersSetters() {
        EmployerStats stats = new EmployerStats();
        stats.setEmployerId(1L);
        assertEquals(1L, stats.getEmployerId());
        stats.setTotalJobs(10);
        assertEquals(10, stats.getTotalJobs());
        stats.setActiveJobs(5);
        assertEquals(5, stats.getActiveJobs());
        stats.setTotalApplications(50);
        assertEquals(50, stats.getTotalApplications());
        stats.setPendingReviews(20);
        assertEquals(20, stats.getPendingReviews());
    }

    @Test
    public void testEmployerStatsAllArgsConstructor() {
        EmployerStats stats = new EmployerStats(1L, 10, 5, 50, 20);
        assertEquals(1L, stats.getEmployerId());
        assertEquals(10, stats.getTotalJobs());
        assertEquals(5, stats.getActiveJobs());
        assertEquals(50, stats.getTotalApplications());
        assertEquals(20, stats.getPendingReviews());
    }

    @Test
    public void testJobViewGettersSettersAndConstructor() {
        Job job = new Job();
        job.setId(1L);
        User user = new User();
        user.setId(2L);
        JobView jv = new JobView(10L, job, user, LocalDateTime.now());
        assertEquals(10L, jv.getId());
        assertEquals(job, jv.getJob());
        assertEquals(user, jv.getUser());
        Job job2 = new Job();
        job2.setId(3L);
        jv.setId(20L);
        jv.setJob(job2);
        User user2 = new User();
        jv.setUser(user2);
        assertEquals(20L, jv.getId());
        assertEquals(job2, jv.getJob());
        assertEquals(user2, jv.getUser());
    }

    @Test
    public void testApplicationNotesGettersSettersAndConstructor() {
        Applications app = new Applications();
        app.setId(1L);
        ApplicationNotes notes = new ApplicationNotes(app, "Test note");
        assertEquals(app, notes.getApplication());
        assertEquals("Test note", notes.getNote());
        assertNotNull(notes.getCreatedAt());
        ApplicationNotes notes2 = new ApplicationNotes();
        notes2.setId(5);
        assertEquals(5, notes2.getId());
        notes2.setApplication(app);
        assertEquals(app, notes2.getApplication());
        notes2.setNote("Another note");
        assertEquals("Another note", notes2.getNote());
        LocalDateTime now = LocalDateTime.now();
        notes2.setCreatedAt(now);
        assertEquals(now, notes2.getCreatedAt());
    }

    @Test
    public void testApplicationStatusHistoryGettersSettersAndConstructor() {
        Applications app = new Applications();
        app.setId(1L);
        ApplicationStatusHistory history = new ApplicationStatusHistory(app, "APPLIED", "REVIEWED");
        assertEquals(app, history.getApplication());
        assertEquals("APPLIED", history.getOldStatus());
        assertEquals("REVIEWED", history.getNewStatus());
        assertNotNull(history.getChangedAt());
        ApplicationStatusHistory h2 = new ApplicationStatusHistory();
        h2.setId(10);
        assertEquals(10, h2.getId());
        h2.setApplication(app);
        assertEquals(app, h2.getApplication());
        h2.setOldStatus("NEW");
        assertEquals("NEW", h2.getOldStatus());
        h2.setNewStatus("OLD");
        assertEquals("OLD", h2.getNewStatus());
        LocalDateTime now = LocalDateTime.now();
        h2.setChangedAt(now);
        assertEquals(now, h2.getChangedAt());
    }

    @Test
    public void testWithdrawalReasonsGettersSettersAndConstructor() {
        Applications app = new Applications();
        app.setId(1L);
        WithdrawalReasons wr = new WithdrawalReasons(app, "Changed mind");
        assertEquals(app, wr.getApplication());
        assertEquals("Changed mind", wr.getReason());
        WithdrawalReasons wr2 = new WithdrawalReasons();
        wr2.setId(5);
        assertEquals(5, wr2.getId());
        wr2.setApplication(app);
        assertEquals(app, wr2.getApplication());
        wr2.setReason("Other reason");
        assertEquals("Other reason", wr2.getReason());
    }

    @Test
    public void testJobSkillGettersSettersAndConstructor() {
        Job job = new Job();
        job.setId(1L);
        JobSkill js = new JobSkill(10, job, "Java");
        assertEquals(10, js.getId());
        assertEquals(job, js.getJob());
        assertEquals("Java", js.getSkill());
        JobSkill js2 = new JobSkill();
        js2.setId(20);
        assertEquals(20, js2.getId());
        Job job2 = new Job();
        js2.setJob(job2);
        assertEquals(job2, js2.getJob());
        js2.setSkill("Python");
        assertEquals("Python", js2.getSkill());
    }

    @Test
    public void testJobSeekerGettersSettersAndConstructor() {
        User user = new User();
        user.setId(1L);
        JobSeeker js = new JobSeeker(1L, "Active", 5, user);
        assertEquals(1L, js.getUserId());
        assertEquals("Active", js.getCurrentStatus());
        assertEquals(5, js.getTotalExperience());
        assertEquals(user, js.getUser());
        JobSeeker js2 = new JobSeeker();
        js2.setUserId(2L);
        assertEquals(2L, js2.getUserId());
        js2.setCurrentStatus("Inactive");
        assertEquals("Inactive", js2.getCurrentStatus());
        js2.setTotalExperience(10);
        assertEquals(10, js2.getTotalExperience());
        User user2 = new User();
        js2.setUser(user2);
        assertEquals(user2, js2.getUser());
    }

    @Test
    public void testEmployerGettersSettersAndConstructor() {
        User user = new User();
        user.setId(1L);
        Employer emp = new Employer(1L, "Acme", "Tech", "100-500",
                "A great company", "https://acme.com", "NYC", user);
        assertEquals(1L, emp.getUserId());
        assertEquals("Acme", emp.getCompanyName());
        assertEquals("Tech", emp.getIndustry());
        assertEquals("100-500", emp.getCompanySize());
        assertEquals("A great company", emp.getDescription());
        assertEquals("https://acme.com", emp.getWebsite());
        assertEquals("NYC", emp.getLocation());
        assertEquals(user, emp.getUser());
        Employer emp2 = new Employer();
        emp2.setUserId(2L);
        assertEquals(2L, emp2.getUserId());
        emp2.setCompanyName("Beta");
        assertEquals("Beta", emp2.getCompanyName());
        emp2.setIndustry("Finance");
        assertEquals("Finance", emp2.getIndustry());
        emp2.setCompanySize("500+");
        assertEquals("500+", emp2.getCompanySize());
        emp2.setDescription("desc");
        assertEquals("desc", emp2.getDescription());
        emp2.setWebsite("https://beta.com");
        assertEquals("https://beta.com", emp2.getWebsite());
        emp2.setLocation("LA");
        assertEquals("LA", emp2.getLocation());
        User user2 = new User();
        emp2.setUser(user2);
        assertEquals(user2, emp2.getUser());
    }

    @Test
    public void testSavedJobGettersSettersAndConstructor() {
        User user = new User();
        user.setId(1L);
        Job job = new Job();
        job.setId(2L);
        SavedJob sj = new SavedJob(10L, user, job, LocalDateTime.now());
        assertEquals(10L, sj.getId());
        assertEquals(user, sj.getUser());
        assertEquals(job, sj.getJob());
        SavedJob sj2 = new SavedJob();
        sj2.setId(20L);
        assertEquals(20L, sj2.getId());
        User user2 = new User();
        sj2.setUser(user2);
        assertEquals(user2, sj2.getUser());
        Job job2 = new Job();
        sj2.setJob(job2);
        assertEquals(job2, sj2.getJob());
    }

    @Test
    public void testApplicationsConstructorAndGettersSetters() {
        Job job = new Job();
        job.setId(1L);
        User seeker = new User();
        seeker.setId(2L);
        ResumeFiles resume = new ResumeFiles();
        resume.setId(3L);
        Applications app = new Applications(100L, job, seeker, resume, "cover", ApplicationStatus.APPLIED);
        assertEquals(100L, app.getId());
        assertEquals(job, app.getJob());
        assertEquals(seeker, app.getSeeker());
        assertEquals(resume, app.getResumeFile());
        assertEquals("cover", app.getCoverLetter());
        assertEquals(ApplicationStatus.APPLIED, app.getStatus());
        assertNotNull(app.getAppliedAt());
        Applications app2 = new Applications(200L, job, seeker, resume, "letter", null);
        assertEquals(ApplicationStatus.APPLIED, app2.getStatus());
        Applications app3 = new Applications();
        app3.setId(300L);
        assertEquals(300L, app3.getId());
        app3.setJob(job);
        assertEquals(job, app3.getJob());
        app3.setSeeker(seeker);
        assertEquals(seeker, app3.getSeeker());
        app3.setResumeFile(resume);
        assertEquals(resume, app3.getResumeFile());
        app3.setCoverLetter("test");
        assertEquals("test", app3.getCoverLetter());
        app3.setStatus(ApplicationStatus.UNDER_REVIEW);
        assertEquals(ApplicationStatus.UNDER_REVIEW, app3.getStatus());
        LocalDateTime now = LocalDateTime.now();
        app3.setAppliedAt(now);
        assertEquals(now, app3.getAppliedAt());
    }


    @Test
    public void testLoginRequestEqualsBranches() {
        org.example.revhire.dto.request.LoginRequest lr1 = new org.example.revhire.dto.request.LoginRequest("a@b.com",
                "pass");
        org.example.revhire.dto.request.LoginRequest lr2 = new org.example.revhire.dto.request.LoginRequest("a@b.com",
                "pass");
        org.example.revhire.dto.request.LoginRequest lr3 = new org.example.revhire.dto.request.LoginRequest("x@y.com",
                "other");

        assertEquals(lr1, lr2);
        assertEquals(lr1.hashCode(), lr2.hashCode());
        assertNotEquals(lr1, lr3);
        assertNotEquals(lr1, null);
        assertNotEquals(lr1, new Object());
        assertEquals(lr1, lr1);
        assertNotNull(lr1.toString());
    }

    @Test
    public void testApplicationRequestEqualsBranches() {
        org.example.revhire.dto.request.ApplicationRequest ar1 = new org.example.revhire.dto.request.ApplicationRequest();
        org.example.revhire.dto.request.ApplicationRequest ar2 = new org.example.revhire.dto.request.ApplicationRequest();
        ar1.setJobId(1L);
        ar1.setSeekerId(2L);
        ar2.setJobId(1L);
        ar2.setSeekerId(2L);
        assertEquals(ar1, ar2);
        assertEquals(ar1.hashCode(), ar2.hashCode());

        ar2.setJobId(99L);
        assertNotEquals(ar1, ar2);
        assertNotNull(ar1.toString());
    }

    @Test
    public void testRegistrationRequestEqualsBranches() {
        org.example.revhire.dto.request.RegistrationRequest rr1 = new org.example.revhire.dto.request.RegistrationRequest();
        org.example.revhire.dto.request.RegistrationRequest rr2 = new org.example.revhire.dto.request.RegistrationRequest();
        rr1.setEmail("a@b.com");
        rr2.setEmail("a@b.com");
        assertEquals(rr1, rr2);
        assertEquals(rr1.hashCode(), rr2.hashCode());

        rr2.setEmail("x@y.com");
        assertNotEquals(rr1, rr2);
        assertNotNull(rr1.toString());
    }

    @Test
    public void testJobPostRequestEqualsBranches() {
        org.example.revhire.dto.request.JobPostRequest jp1 = new org.example.revhire.dto.request.JobPostRequest();
        org.example.revhire.dto.request.JobPostRequest jp2 = new org.example.revhire.dto.request.JobPostRequest();
        jp1.setTitle("Dev");
        jp2.setTitle("Dev");
        assertEquals(jp1, jp2);
        assertEquals(jp1.hashCode(), jp2.hashCode());

        jp2.setTitle("QA");
        assertNotEquals(jp1, jp2);
        assertNotNull(jp1.toString());
    }

    @Test
    public void testAuthResponseEqualsBranches() {
        org.example.revhire.dto.response.AuthResponse a1 = new org.example.revhire.dto.response.AuthResponse(1L, "Test",
                "a@b.com", Role.EMPLOYER, "token");
        org.example.revhire.dto.response.AuthResponse a2 = new org.example.revhire.dto.response.AuthResponse(1L, "Test",
                "a@b.com", Role.EMPLOYER, "token");
        org.example.revhire.dto.response.AuthResponse a3 = new org.example.revhire.dto.response.AuthResponse(2L,
                "Other", "x@y.com", Role.JOB_SEEKER, "tok2");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotEquals(a1, a3);
        assertNotNull(a1.toString());
    }

    @Test
    public void testApplicationResponseEqualsBranches() {
        org.example.revhire.dto.response.ApplicationResponse r1 = new org.example.revhire.dto.response.ApplicationResponse();
        org.example.revhire.dto.response.ApplicationResponse r2 = new org.example.revhire.dto.response.ApplicationResponse();
        r1.setId(1L);
        r2.setId(1L);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        r2.setId(99L);
        assertNotEquals(r1, r2);
        assertNotNull(r1.toString());
    }

    @Test
    public void testJobResponseEqualsBranches() {
        org.example.revhire.dto.response.JobResponse j1 = new org.example.revhire.dto.response.JobResponse();
        org.example.revhire.dto.response.JobResponse j2 = new org.example.revhire.dto.response.JobResponse();
        j1.setId(1L);
        j2.setId(1L);
        assertEquals(j1, j2);
        assertEquals(j1.hashCode(), j2.hashCode());

        j2.setId(99L);
        assertNotEquals(j1, j2);
        assertNotNull(j1.toString());
    }

    @Test
    public void testUserResponseEqualsBranches() {
        org.example.revhire.dto.response.UserResponse u1 = new org.example.revhire.dto.response.UserResponse();
        org.example.revhire.dto.response.UserResponse u2 = new org.example.revhire.dto.response.UserResponse();
        u1.setId(1L);
        u1.setEmail("a@b.com");
        u2.setId(1L);
        u2.setEmail("a@b.com");
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());

        u2.setId(99L);
        assertNotEquals(u1, u2);
        assertNotNull(u1.toString());
    }

    @Test
    public void testResumeResponseEqualsBranches() {
        org.example.revhire.dto.response.ResumeResponse rr1 = new org.example.revhire.dto.response.ResumeResponse();
        org.example.revhire.dto.response.ResumeResponse rr2 = new org.example.revhire.dto.response.ResumeResponse();
        org.example.revhire.dto.response.ResumeObjectiveResponse obj1 = new org.example.revhire.dto.response.ResumeObjectiveResponse();
        org.example.revhire.dto.response.ResumeObjectiveResponse obj2 = new org.example.revhire.dto.response.ResumeObjectiveResponse();
        rr1.setObjective(obj1);
        rr2.setObjective(obj1);
        assertEquals(rr1, rr2);
        assertEquals(rr1.hashCode(), rr2.hashCode());

        rr2.setObjective(obj2);
        rr1.equals(rr2);
        assertNotNull(rr1.toString());
    }
}
