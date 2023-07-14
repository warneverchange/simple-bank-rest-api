package com.evilcorp.api.servcies;

import com.evilcorp.repositories.BankRepository;
import com.evilcorp.services.BankService;
import com.evilcorp.services.BankServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BankServiceTests {
    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    @Test
    public void test() {

    }
}
