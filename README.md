## 组件开发说明

### 1. 组件定义
组件是SoFlu全自动开发平台的精髓所在，与函数、流程、资源相配合可实现各种复杂功能。SoFlu全自动开发平台提供了基础、SQL、通信、工具、数据库、脚本语言等各种类型的组件，以帮助用户实现各种功能需求。同时平台也提供了组件的开发包及开发说明文档，当平台组件无法满足用户开发需求时，用户可以通过对入参、出参的定义，衔接整体流程，开发出能实现特定功能的组件。

### 2. 组件设计原则
为了开发出满足特定功能的组件，用户需要先了解组件的设计原则，主要分为以下六点：

#### 2.1 解决共性问题
在需求实现中，对重复出现、有相似性的问题进行总结分析，剥离各问题的特性，抽象出共性，确定组件要解决的问题，而非个性问题。

#### 2.2 不依赖特定环境，具有普适性
组件运行环境基于JDK版本，可能运行的环境包括各种操作系统，功能不能只适配某特定环境，应适应多种目标环境。

#### 2.3 配置信息与平台资源契合
按平台资源实例所定义资源的规范标准，开发组件中需要的配置项。

#### 2.4 性能优化，最佳实践
对比测试各种实现方式后，采取最优策略来实现功能，不能为了追求开发速度，牺牲组件的性能。

#### 2.5 健壮性，异常处理合理性
组件应用到平台后，会运行在各类复杂环境，在异常处理中需充分考虑，返回合理的异常，返回消息时明确组件内部处于何种状态。

#### 2.6 质量与安全
需确保组件代码及引用的相关JAR包，均通过了相关漏洞测试，安全审核；在代码质量上起码要达到平台要求，同时追求高质量代码。
