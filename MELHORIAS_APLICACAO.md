# 🚀 Melhorias na Aplicação de Tarefas - Spring Boot

---

## **SLIDE 1: Título e Introdução**

# Melhorias na Aplicação de Tarefas
### Spring Boot + Thymeleaf
**Professor: Ryan**  
**Disciplina: Web 2 - IFSC**

---

## **SLIDE 2: O que é a Aplicação**

### 📋 Sistema de Gerenciamento de Tarefas

- ✅ **Tarefas**: Criar, editar, excluir, listar
- ✅ **Categorias**: Organizar tarefas por tipo
- ✅ **Associações**: Vincular tarefas a categorias
- ✅ **Interface Web**: Thymeleaf + CSS responsivo

---

## **SLIDE 3: O que Melhoramos**

### 🎯 **Frentes de Melhoria:**

1. **🎨 CSS** - Visual moderno e responsivo
2. **🖥️ Templates HTML** - Estrutura semântica
3. **⚙️ Services** - Código otimizado
4. **🎭 Template Services** - Validações robustas

---

## **SLIDE 4: CSS - Antes vs Depois**

### **❌ ANTES:**
- CSS básico e simples
- Sem responsividade
- Visual pouco atrativo

### **✅ DEPOIS:**
- Design moderno com sombras
- Responsivo para mobile
- Cores para status/prioridades
- Transições suaves

---

## **SLIDE 5: Templates HTML - Lista**

### **❌ ANTES:**
```html
<table border="1" cellpadding="6">
  <tr>
    <td th:text="${t.titulo}">Titulo</td>
    <td th:text="${t.prioridade}">MEDIA</td>
  </tr>
</table>
```

### **✅ DEPOIS:**
```html
<div class="container">
    <h1>📋 Minhas Tarefas</h1>
    <table>
        <thead>
            <tr>
                <th>Título</th>
                <th>Prioridade</th>
                <th>Status</th>
            </tr>
        </thead>
    </table>
</div>
```

---

## **SLIDE 6: Templates HTML - Formulário**

### **❌ ANTES:**
```html
<form method="post">
  <label>Título</label><br/>
  <input th:field="*{titulo}"/>
  <button>Salvar</button>
</form>
```

### **✅ DEPOIS:**
```html
<div class="container">
    <h1>📝 Nova Tarefa</h1>
    <form method="post">
        <div class="form-group">
            <label for="titulo">📝 Título *</label>
            <input type="text" required/>
        </div>
        <button class="btn btn-success">💾 Salvar</button>
    </form>
</div>
```

---

## **SLIDE 7: Services - Otimizações**

### **❌ ANTES:**
```java
@PutMapping("/{id}")
public ResponseEntity<Categoria> atualizar(Long id, Categoria dto) {
    if (!repo.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    Categoria categoria = repo.findById(id).orElse(null);
    // ... mais código
}
```

### **✅ DEPOIS:**
```java
@PutMapping("/{id}")
public ResponseEntity<Categoria> atualizar(Long id, Categoria dto) {
    return repo.findById(id)
            .map(categoria -> {
                categoria.setNome(dto.getNome());
                return ResponseEntity.ok(repo.save(categoria));
            })
            .orElse(ResponseEntity.notFound().build());
}
```

---

## **SLIDE 8: Repositórios - Novos Métodos**

### **CategoriaRepository:**
```java
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
    boolean existsByNome(String nome);
}
```

### **TarefaRepository:**
```java
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByStatus(Status status);
    List<Tarefa> findByPrioridade(Prioridade prioridade);
    List<Tarefa> findByResponsavel(String responsavel);
    List<Tarefa> findByDataLimiteBefore(LocalDate data);
}
```

---

## **SLIDE 9: Template Services - Validações**

### **TemplateService - Validação:**
```java
@PostMapping
String salvar(Tarefa tarefa, Model model) {
    if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
        model.addAttribute("erro", "Título é obrigatório");
        return "formulario";
    }
    
    repo.save(tarefa);
    return "redirect:/templates";
}
```

### **TemplateCategoriaService - Proteção:**
```java
@PostMapping("/{id}/excluir")
String excluirCategoria(Long id) {
    var categoria = categoriaRepo.findById(id);
    if (categoria.get().getTarefas().size() > 0) {
        return "redirect:/templates-categoria";
    }
    categoriaRepo.deleteById(id);
    return "redirect:/templates-categoria";
}
```

---

## **SLIDE 10: Benefícios das Melhorias**

### **🎯 Para o Professor:**
- Código profissional para demonstrações
- Aplicação robusta que não quebra
- Exemplos de boas práticas

### **👨‍🎓 Para os Alunos:**
- Interface visual atrativa
- Experiência responsiva
- Exemplos de validação

### **🚀 Para a Aplicação:**
- Maior segurança
- Código organizado
- UX melhorada

---

## **SLIDE 11: Conceitos Demonstrados**

### **Spring Boot:**
- Controllers para templates
- Repositories customizados
- Services com lógica de negócio
- Validação e tratamento de erros

### **Thymeleaf:**
- Integração com CSS
- Iteração e condicionais
- Links e formulários
- Mensagens flash

---

## **SLIDE 12: Novas Melhorias Implementadas**

### **✅ Funcionalidades Adicionadas:**
- 🔍 **Sistema de busca e filtros** por título, responsável, status e prioridade
- ✅ **Validações Bean Validation** com mensagens personalizadas
- 💬 **Flash Messages** para feedback do usuário
- 🎨 **Interface Bootstrap 5** moderna e responsiva

### **📱 Melhorias de UX:**
- Filtros responsivos com Bootstrap
- Alertas dismissíveis
- Badges coloridos para status e prioridades
- Layout mobile-first

---

## **SLIDE 13: Detalhes das Novas Funcionalidades**

### **🔍 Sistema de Filtros:**
```java
@GetMapping
String listar(Model model, 
              @RequestParam(required = false) String titulo,
              @RequestParam(required = false) String responsavel,
              @RequestParam(required = false) Status status,
              @RequestParam(required = false) Prioridade prioridade) {
    // Filtros implementados com Stream API
}
```

### **✅ Validações Bean Validation:**
```java
@NotBlank(message = "O título é obrigatório")
@Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
private String titulo;

@FutureOrPresent(message = "A data limite deve ser hoje ou uma data futura")
private LocalDate dataLimite;
```

### **💬 Flash Messages:**
```java
redirectAttributes.addFlashAttribute("success", 
    tarefa.getId() != null ? "Tarefa atualizada com sucesso!" : "Tarefa criada com sucesso!");
```

---

## **SLIDE 14: Próximos Passos Futuros**

### **Funcionalidades Futuras:**
- 📊 Dashboard com estatísticas
- 👥 Sistema de usuários
- 📅 Calendário de tarefas
- 🔔 Notificações

### **Melhorias Técnicas:**
- 🧪 Testes unitários
- 📝 Documentação da API
- 🔒 Segurança com Spring Security
- 🚀 Deploy em produção

---

## **SLIDE 15: Resumo Final**

### **🎯 O que Conseguimos:**

Transformamos uma aplicação básica em um **sistema profissional**:

- 🎨 **Interface moderna** com Bootstrap 5
- ⚙️ **Backend otimizado** com validações Bean Validation
- 🛡️ **Tratamento de erros** robusto
- 💬 **Feedback claro** com flash messages
- 🔍 **Sistema de filtros** avançado
- 📱 **Responsivo** para todos os dispositivos

### **✅ Resultado:**
Aplicação **pronta para demonstração em aula** com funcionalidades modernas!

---

## **SLIDE 16: Agradecimentos**

# Obrigado! 🎓

### **✅ Projeto Atualizado e Pronto!**
- Validações Bean Validation implementadas
- Flash messages funcionando
- Sistema de filtros ativo
- Interface Bootstrap moderna

### **🚀 Próxima Aula:**
- Aplicação funcional e robusta
- Código limpo e organizado
- Exemplos práticos de validação

---

*Material criado para as aulas de Web 2 - IFSC*  
*Professor: Ryan - 2024*
