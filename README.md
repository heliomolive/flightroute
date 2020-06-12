# Flight Route
API para encontrar a rota mais barata entre dois pontos.

Esta aplicação foi criada com Spring Boot e disponibiliza dois endpoints, um para consultar a melhor rota e o outro 
permite cadastrar uma nova rota. É considerada a melhor rota aquela de menor custo, independente de 
outras características, como o número de conexões.

As rotas diretas entre dois pontos são mantidas em um arquivo. O endereço deste arquivo é configurado através da 
propriedade `input.file`, que é definida no arquivo `application.properties` mas também pode ser informada como parâmetro 
na execução via linha de comando.

Quando executada via linha de comando, a aplicação permite que o usuário faça consultas digitando a rota desejada.

## Execução
A aplicação pode ser executada através do arquivo JAR:
> java -jar flightroute-0.0.1-SNAPSHOT.jar --input.file=input-routes.csv

Ou mesmo com o suporte de uma IDE. Na implementação foi utilizado o IntelliJ.

## Apresentação da API e uso dos endpoints

### GET http://localhost:8080/flightroute/v1/route?source=v0&dest=v8

Consulta a rota mais barata entre os dois pontos. Os parâmetros `source` e `dest` são obrigatórios. O retorno é um JSON 
com o custo da rota e suas conexões (em caso de sucesso, retorna o código HTTP 200).

### POST http://localhost:8080/flightroute/v1/route

Registra uma nova rota direta entre dois pontos. É obrigatório passar uma requisição conforme exemplo a seguir e também incluir 
o _header_ `Content-Type: application/json` (os três atributos da requisição são requeridos):
> {
  	"source":"V0",
  	"dest":"V8",
  	"cost":17.00
  }

Em caso de sucesso, o endpoint retorna o código HTTP 201, sem objeto de retorno. A nova rota é registrada no arquivo 
`input.file`, ficando disponível também em futuras execuções.

## Retornos da API em casos de erro

Os endpoints da API possuem um formato padrão para retornos de erro. Exemplo: 
> {
      "developerMessage": "Invalid source [v11]",
      "userMessage": "Origem inválida: v11"
  }

O atributo `developerMessage` apresenta uma mensagem mais técnica, podendo ser utilizado pelo desenvolvedor responsável pela 
aplicação que vai consumir a API. O `userMessage` é uma mensagem que pode ser exibida ao usuário final -- as mensagens de 
usuário estão no arquivo `messages.properties`.

## Detalhes da solução adotada

### Algoritmo para descoberta da melhor rota

Foi adotado o algoritmo do caminho mínimo de Dijkstra (https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/).

Este é um algoritmo clássico para resolver o problema de melhor rota. Dado um grafo com as distâncias entre seus vértices, 
o algoritmo calcula a melhor rota, a partir de uma origem informada, para todos os destinos (todos os demais vértices). 

Na solução implementada, foi utilizada a estratégia de `single target`. Trata-se de um pequeno ajuste no algoritmo, 
forçando sua parada quando a melhor rota para o destino solicitado for encontrada.

### Visão geral da arquitetura

A aplicação está organizada em camadas: _WEB controller_, serviço e acesso a dados (que faz acesso ao arquivo de entrada). 

Na camada do _controller_ foi criado um _Controller Advice_ para o tratamento de exceções, incluindo _log_ das mesmas. O 
_controller_ `RouteController` disponibiliza os dois endpoints implementados. 

A classe de serviço `RouteServiceImpl` é responsável por manter o mapa de rotas. Ela implementa métodos para acesso ao 
mapa, incluindo a busca da melhor rota e o registro de novas rotas. Também é responsável por carregar o arquivo de 
entrada as rotas conhecidas (para isso, usa um componente da camada de dados), bem como algumas validações aplicadas na 
execução dos _endpoints_.

A implementação do algoritmo de melhor rota está na classe `DijkstraRouteMap`. Esta classe mantém um _hash map_ contendo 
os vértices conhecidos e também uma matriz com as melhores rotas. 

O objeto `Route` mantém os dados de uma rota, incluindo origem, destino, custo e conexões. Também foi incluído nesse objeto 
a _flag_ `costVerified` que indica se o cálculo de melhor rota já foi realizado para essa origem e destino -- quando é 
solicitada uma rota, caso exista uma rota direta entre origem e destino, o sistema verifica se tal rota direta possui o 
menor custo. Assim, sempre é retornada a melhor rota, mesmo que esta possua conexões e exista uma outra direta.

Na camada de acesso a dados, com o objetivo de estruturar melhor a aplicação, foi disponibilizado um componente para 
manipular o arquivo de entradas (`InputRouteFileLoader`) e classes específicas de leitura e escrita no arquivo.

### Comunicação entre as camadas

Classes de camadas inferiores desconhecem objetos de camadas superiores. Por exemplo, para registrar uma nova rota, a 
camada de serviço recebe apenas os parâmetros necessários, não recebe o objeto de requisição tratado pelo _controller_. 

Foi criado um _mapper_ para fazer as conversões necessárias entre objetos de camadas diferentes. Atualmente, este _mapper_ 
apenas é utilizado apenas para converter um objeto `Route` em um objeto de resposta da camada de _controller_. Contudo, 
o objetivo é utilizar futuramente essa estratégia para diminuir a dependência entre as camadas.

Ainda sobre objetos do _controller_, é uma boa prática criar classes específicas para requisições e respostas. Essas classes 
fazem parte da interface dos endpoints. Desta forma, mantemos o desacoplamento dos endpoints com objetos de outras camadas. 
Além disso, podemos usar nas classes de requisição e resposta anotações específicas da camada de _controller_ (por exemplo, 
anotações para validação de parâmetros das requisições e para documentação da API).

### Carregamento do arquivo de rotas, consultas e registro de nova rota

Ao iniciar, a aplicação carrega as rotas diretas do arquivo de entrada (`input-file`). 

Quando a melhor rota entre determinada origem e destino é calculada, esta rota é mantida em memória na matriz de melhores 
rotas (classe `DijkstraRouteMap`). Caso a consulta seja solicitada novamente, o sistema retorna a rota já calculada 
previamente.

Quando uma nova rota é registrada, é necessário gravá-la no arquivo de entrada e também reiniciar a matriz de melhores 
rotas, pois tal conexão pode representar uma alternativa melhor para alguma rota entre dois pontos quaisquer. Assim, 
sempre que registra uma nova rota, o sistema recarrega o arquivo de entrada e consultas futuras demandarão novos 
cálculos de melhor rota.
 
### Testes automatizados

Em sistemas organizados em camadas, uma boa estratégia é criar testes em cada camada da aplicação:
* Testes na camada de acesso a dados: validam as classes específicas desta camada (caso seja utilizado JPA, por exemplo, 
pode-se validar o mapeamento objeto-relacional das entidades e os métodos criados nos repositórios).
* Testes na camada de serviço: são testes unitários que utilizam apenas JUnit (assim executam com melhor desempenho, por 
não carregarem o contexto do Spring) e criam _mocks_ das suas dependências.
* Testes na camada de _controller_: foram criados com suporte da anotação `@WebMvcTest`, para carregar somente o contexto 
mínimo necessário do Spring, e também utilizam _mocks_.
* Testes de integração: utilizam a anotação `@SpringBootTest` para carregar o contexto completo do Spring e executar 
testes que percorrem todas as camadas da aplicação (desde a camada HTTP até acesso a dados).

Contudo, para respeitar o prazo para entrega do projeto, não foram criados testes para a camada de acesso a dados. Em 
todas as demais camadas foram criados testes automatizados, embora a cobertura não seja ainda a ideal (o objetivo foi 
disponibilizar um conjunto mínimo de testes e exemplificar a estratégia de testes por camada).

### Considerações finais

No desenvolvimento da solução, decidiu-se utilizar Spring Boot tendo em vista a necessidade de expor uma API Web. Além de 
oferecer suporte à criação de microserviços, o Spring Boot permite que um servidor Web embutido seja entregue junto com 
a aplicação, tornando a sua execução muito mais simples. Sem isso, seria necessário o uso de um servidor Web externo.

O uso de frameworks e bibliotecas externas limitou-se a um pacote mínimo, composto pelas dependências requeridas pelo 
Spring Web, que oferecem suporte ao desenvolvimento de APIs Restful.

A seguir listamos outras dependências, não utilizadas neste projeto, mas que poderiam ser incluídas;
* Swagger: anotações para documentar a API
* Lombok: ferramenta para geração de código, permite a redução de código _boilerplate_ (métodos _set_, _get_, construtores,
_builders_, etc)
* MapStruct: ferramenta para geração de código, facilita a criação de _mappers_

