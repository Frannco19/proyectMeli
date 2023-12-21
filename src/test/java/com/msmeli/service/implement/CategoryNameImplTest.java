package com.msmeli.service.implement;

import com.msmeli.repository.CategoryNameRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.utbot.runtime.utils.java.UtUtils.setField;

@ExtendWith(MockitoExtension.class)
public final class CategoryNameImplTest {
    @InjectMocks
    private CategoryNameImpl categoryNameImpl;

    @Mock
    private CategoryNameRepository categoryNameRepositoryMock;

    private AutoCloseable mockitoCloseable;

    ///region Test suites for executable com.msmeli.service.implement.CategoryNameImpl.findAll

    ///region SYMBOLIC EXECUTION: SUCCESSFUL EXECUTIONS for method findAll()

    /**
     * @utbot.classUnderTest {@link CategoryNameImpl}
     * @utbot.methodUnderTest {@link CategoryNameImpl#findAll()}
     * @utbot.invokes {@link CategoryNameRepository#findAll()}
     * @utbot.returnsFrom {@code return categoryNameRepository.findAll();}
     */
    @Test
    @DisplayName("findAll: CategoryNameRepositoryFindAll -> return categoryNameRepository.findAll()")
    public void testFindAll_CategoryNameRepositoryFindAll() {
        (when(categoryNameRepositoryMock.findAll())).thenReturn(((List) null));

        List actual = categoryNameImpl.findAll();

        assertNull(actual);
    }

    @Test
    @DisplayName("findAll: return categoryNameRepository.findAll() : True -> ThrowNullPointerException")
    public void testFindAll_ThrowNullPointerException() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        setField(categoryNameImpl, "com.msmeli.service.implement.CategoryNameImpl", "categoryNameRepository", null);
        
        /* This test fails because method [com.msmeli.service.implement.CategoryNameImpl.findAll] produces [java.lang.NullPointerException]
            com.msmeli.service.implement.CategoryNameImpl.findAll(CategoryNameImpl.java:19) */
        categoryNameImpl.findAll();
    }

    @Test
    @DisplayName("findAll: ")
    @Timeout(value = 1000L, unit = TimeUnit.MILLISECONDS)
    public void testFindAll() {
        List list = emptyList();
        (when(categoryNameRepositoryMock.findAll())).thenReturn(list);
        
        /* This execution may take longer than the 1000 ms timeout
         and therefore fail due to exceeding the timeout. */
        assertTimeoutPreemptively(Duration.ofMillis(1000L), () -> categoryNameImpl.findAll());
    }



    @BeforeEach
    public void setUp() {
        mockitoCloseable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockitoCloseable.close();
    }

}
