# tarefas-plus (Android)

Este é um aplicativo de lista de tarefas, desenvolvido em **Kotlin** para a plataforma **Android**. Ele foi criado com o objetivo de demonstrar minhas habilidades em desenvolvimento mobile e meu conhecimento sobre os conceitos fundamentais da plataforma.

O aplicativo utiliza o **Firebase** para gerenciamento de dados, permitindo que os usuários autentiquem (façam login e registro) e armazenem suas tarefas de forma segura.

### 📋 Funcionalidades

* **Autenticação de Usuário:** O usuário pode criar uma conta, fazer login e redefinir sua senha utilizando o Firebase Authentication.
* **Gerenciamento de Tarefas:**
    * Criação de novas tarefas.
    * Visualização da lista de tarefas existentes.
    * Edição de tarefas.
    * Exclusão de tarefas.
* **Persistência de Dados:** Todas as tarefas são salvas no **Firebase Realtime Database**, garantindo que o progresso do usuário seja mantido mesmo após fechar o aplicativo.
* **Navegação e Layout Dinâmico:** A navegação entre as telas é gerenciada de forma eficiente utilizando o **Navigation Component** e o **Safe Args**. A visualização das tarefas é feita em um **ViewPager**, com rolagem horizontal entre as categorias ("todo", "doing", "done").

### 🛠️ Tecnologias Utilizadas

* **Linguagem de Programação:** Kotlin
* **Plataforma:** Android
* **Interface de Usuário:** XML
* **Serviços de Backend:** Firebase Authentication e Realtime Database
* **Bibliotecas e Padrões:**
    * Navigation Component e Safe Args
    * ViewBinding
    * RecyclerView e Adapter
    * ViewPager e FragmentStateAdapter
    * ViewModel
    * **BottomSheet** (utilizado para confirmar ações importantes, como a exclusão de tarefas, e fornecer opções contextuais ao usuário)
    * **Toast** (para mensagens rápidas e informativas ao usuário)

### 🚀 Aprendizados e Futuros Desafios

Durante o desenvolvimento deste projeto, aprofundei meu conhecimento em navegação, persistência de dados e na utilização de ferramentas como o **ViewBinding** e o **RecyclerView**, que me permitiram escrever um código mais limpo e eficiente. O desafio de implementar o **ViewPager** para separar as tarefas por categoria foi uma excelente oportunidade para aplicar conceitos de navegação e gerenciamento de estados.

Para este projeto, comecei a explorar o padrão **ViewModel**, que ainda será abordado em detalhes no meu curso. Eu o implementei no código para me familiarizar com sua estrutura e me preparar para aprender sobre arquitetura MVVM no futuro. Este projeto representa meu compromisso com o aprendizado contínuo e minha curiosidade por novas tecnologias.

---

### 💻 Como Executar o Projeto

1.  Clone este repositório para a sua máquina local.
2.  Abra o projeto no Android Studio.
3.  Sincronize o projeto com os arquivos do Gradle.
4.  Configure o Firebase no seu projeto (Adicionar o `google-services.json`).
5.  Execute o aplicativo em um emulador ou em um dispositivo físico.
