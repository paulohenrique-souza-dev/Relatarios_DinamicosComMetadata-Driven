package com.relatoriosjava.RelatoriosJava.repository;
import com.relatoriosjava.RelatoriosJava.entity.CampoRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CampoRelatorioRepository extends JpaRepository<CampoRelatorio,Long> {
    List<CampoRelatorio> findByRelatorioIdOrderByOrdemAsc(Long relatorioId);
}
