package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.BuildPerformanceDto;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.Ssd;
import com.j2ee.buildpcchecker.entity.SsdType;
import com.j2ee.buildpcchecker.entity.Vga;
import com.j2ee.buildpcchecker.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuildEvaluationServiceTest {

    @Mock private CpuRepository cpuRepository;
    @Mock private VgaRepository vgaRepository;
    @Mock private RamRepository ramRepository;
    @Mock private SsdRepository ssdRepository;
    @Mock private HddRepository hddRepository;
    @Mock private GameRepository gameRepository;
    @Mock private MainboardRepository mainboardRepository;
    @Mock private PsuRepository psuRepository;
    @Mock private CoolerRepository coolerRepository;

    @InjectMocks
    private BuildEvaluationService buildEvaluationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvaluateBuild_HighEnd() {
        // Arrange
        String cpuId = "cpu-id";
        String vgaId = "vga-id";
        String ssdId = "ssd-id";

        Cpu cpu = Cpu.builder().id(cpuId).score(25000).tdp(125).build();
        Vga vga = Vga.builder().id(vgaId).score(30000).tdp(350).build();
        Ssd ssd = Ssd.builder().id(ssdId).ssdType(SsdType.builder().name("NVME").build()).build();

        when(cpuRepository.findById(cpuId)).thenReturn(Optional.of(cpu));
        when(vgaRepository.findById(vgaId)).thenReturn(Optional.of(vga));
        when(ssdRepository.findAllById(anyList())).thenReturn(Collections.singletonList(ssd));
        when(gameRepository.findAll()).thenReturn(Collections.emptyList());

        BuildCheckRequest request = BuildCheckRequest.builder()
                .cpuId(cpuId).vgaId(vgaId).ssdIds(Collections.singletonList(ssdId)).build();

        // Act
        BuildPerformanceDto result = buildEvaluationService.evaluateBuild(request);

        // Assert
        assertTrue(result.getTotalScore() > 20000);
        assertTrue(result.getTier().contains("Professional")); // A (Professional) contains "Professional"
        assertNotNull(result.getSummary());
        assertNotNull(result.getBottleneck());
    }

    @Test
    void testTierCalculation_Baseline() {
        String cpuId = "cpu-mid";
        Cpu cpu = Cpu.builder().id(cpuId).score(20000).tdp(65).build();
        when(cpuRepository.findById(cpuId)).thenReturn(Optional.of(cpu));
        when(ssdRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(gameRepository.findAll()).thenReturn(Collections.emptyList());

        BuildCheckRequest request = BuildCheckRequest.builder().cpuId(cpuId).build();
        
        BuildPerformanceDto result = buildEvaluationService.evaluateBuild(request);
        assertTrue(result.getTier().contains("C")); // "C (Baseline)" contains "C"
    }
}
