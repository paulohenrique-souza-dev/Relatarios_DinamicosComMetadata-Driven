package com.relatoriosjava.RelatoriosJava.repository;
import com.relatoriosjava.RelatoriosJava.entity.ColunaRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ColunaRelatorioRepository extends JpaRepository<ColunaRelatorio,Long> {
    List<ColunaRelatorio> findByRelatorioIdAndVisivelTrueOrderByOrdemAsc(Long relatorioId);
}
