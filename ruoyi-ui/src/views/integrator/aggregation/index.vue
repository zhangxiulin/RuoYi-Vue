<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="聚合名称" prop="agrName">
        <el-input
          v-model="queryParams.agrName"
          placeholder="请输入聚合名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="聚合编号" prop="agrCode">
        <el-input
          v-model="queryParams.agrCode"
          placeholder="请输入聚合编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择启用状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['integrator:aggregation:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['integrator:aggregation:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['integrator:aggregation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['integrator:aggregation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="aggregationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="主键ID" align="center" prop="agrId" />-->
      <el-table-column label="聚合名称" align="center" prop="agrName" />
      <el-table-column label="聚合编号" align="center" prop="agrCode" />
      <el-table-column label="分布式事务" align="center" prop="isDtx" :formatter="isDtxFormat" />
      <el-table-column label="事务方案" align="center" prop="dtxSolution" :formatter="dtxSolutionOptionsFormat" />
      <el-table-column label="是否异步" align="center" prop="isAsync" :formatter="isAsyncFormat" />
      <el-table-column label="执行顺序" align="center" prop="executionOrder" :formatter="executionOrderFormat" />
      <el-table-column label="启用状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip=true  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['integrator:aggregation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['integrator:aggregation:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改服务聚合对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs tab-position="left" style="height: 400px;">
          <el-tab-pane label="基础配置">
            <el-form-item label="聚合名称" prop="agrName">
              <el-input v-model="form.agrName" placeholder="请输入聚合服务名称" />
            </el-form-item>
            <el-form-item label="聚合编号" prop="agrCode">
              <el-input v-model="form.agrCode" placeholder="请输入聚合服务编号" />
            </el-form-item>
            <el-form-item label="是否异步">
              <el-radio-group v-model="form.isAsync">
                <el-radio
                  v-for="dict in isAsyncOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="启用状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="接口与事务">
            <el-form-item label="服务">
              <el-select v-model="form.forwardIds" placeholder="接口列表（有序）" filterable size="medium" multiple >
                <el-option
                  v-for="item in forwardOptions"
                  :key="item.infoId"
                  :label="item.forwardCode"
                  :value="item.infoId" >
                  <span style="float: left">{{ item.forwardName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.forwardCode }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="执行顺序">
              <el-radio-group v-model="form.executionOrder">
                <el-radio
                  v-for="dict in executionOrderOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="分布式事务">
              <el-radio-group v-model="form.isDtx" @change="isDtxChange" >
                <el-radio
                  v-for="dict in isDtxOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="事务方案" prop="dtxSolution"  >
              <el-select v-model="form.dtxSolution" placeholder="请选择事务方案" clearable :disabled="form_dtxSolution_disabled" >
                <el-option
                  v-for="dict in dtxSolutionOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>


        <!--
        <el-form-item label="自定义成功码" disabled prop="rtStatusOk">
          <el-input v-model="form.rtStatusOk" placeholder="请输入自定义成功码" />
        </el-form-item>
        <el-form-item label="自定义失败码" disabled prop="rtStatusError">
          <el-input v-model="form.rtStatusError" placeholder="请输入自定义失败码" />
        </el-form-item> -->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listAggregation, getAggregation, delAggregation, addAggregation, updateAggregation, exportAggregation } from "@/api/integrator/aggregation";

  export default {
    name: "Aggregation",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 服务聚合表格数据
        aggregationList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否分布式事务字典
        isDtxOptions: [],
        // 事务方案字典
        dtxSolutionOptions: [],
        // 是否异步字典
        isAsyncOptions: [],
        // 执行顺序字典
        executionOrderOptions: [],
        // 启用状态字典
        statusOptions: [],
        // 服务接口选项
        forwardOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          agrName: null,
          agrCode: null,
          status: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          agrName: [
            { required: true, message: "聚合名称不能为空", trigger: "blur" }
          ],
          agrCode: [
            { required: true, message: "聚合编号不能为空", trigger: "blur" }
          ],
          isDtx: [
            { required: true, message: "分布式事务不能为空", trigger: "blur" }
          ],
          isAsync: [
            { required: true, message: "是否异步不能为空", trigger: "blur" }
          ],
          executionOrder: [
            { required: true, message: "执行顺序不能为空", trigger: "blur" }
          ],
        },
        form_dtxSolution_disabled: false,
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_yes_no").then(response => {
        this.isDtxOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
          this.isAsyncOptions = response.data;
      });
      this.getDicts("in_execution_order").then(response => {
          this.executionOrderOptions = response.data;
      });
      this.getDicts("sys_normal_disable").then(response => {
          this.statusOptions = response.data;
      });
      this.getDicts("dtx_solution_type").then(response => {
        this.dtxSolutionOptions = response.data;
      });
    },
    methods: {
      /** 查询服务聚合列表 */
      getList() {
        this.loading = true;
        listAggregation(this.queryParams).then(response => {
          this.aggregationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      },
      // 分布式事务字典翻译
      isDtxFormat(row, column) {
        return this.selectDictLabel(this.isDtxOptions, row.isDtx);
      },
      // 分布式事务字典翻译
      dtxSolutionOptionsFormat(row, column) {
        return this.selectDictLabel(this.dtxSolutionOptions, row.dtxSolution);
      },
      // 是否异步字典翻译
      isAsyncFormat(row, column) {
        return this.selectDictLabel(this.isAsyncOptions, row.isAsync);
      },
      // 执行顺序字典翻译
      executionOrderFormat(row, column) {
        return this.selectDictLabel(this.executionOrderOptions, row.executionOrder);
      },
      // 启用状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          agrId: null,
          agrName: null,
          agrCode: null,
          isDtx: "Y",
          dtxSolution: null,
          isAsync: "N",
          executionOrder: "1",
          rtStatusOk: null,
          rtStatusError: null,
          status: "0",
          remark: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          forwardIds: []
        };
        this.resetForm("form");
        this.form_dtxSolution_disabled = false;
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.agrId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        getAggregation().then(response => {
          this.forwardOptions = response.forwardInfos;
        });
        this.open = true;
        this.title = "添加服务聚合";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const agrId = row.agrId || this.ids
        getAggregation(agrId).then(response => {
          this.forwardOptions = response.forwardInfos;
          this.form = response.data;
          this.form.forwardIds = response.forwardIds;
          this.open = true;
          this.title = "修改服务聚合";
          this.controlDisable(this.form.isDtx);
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.agrId != null) {
              updateAggregation(this.form).then(response => {
                this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
            } else {
              addAggregation(this.form).then(response => {
                this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const agrIds = row.agrId || this.ids;
        this.$confirm('是否确认删除服务聚合编号为"' + agrIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAggregation(agrIds);
        }).then(() => {
          this.getList();
        this.msgSuccess("删除成功");
      })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有服务聚合数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAggregation(queryParams);
        }).then(response => {
          this.download(response.msg);
      })
      },
      /**转发类型改变**/
      isDtxChange(item) {
        this.controlDisable(item);
      },

      controlDisable(isDtx){
        if (isDtx == "Y") {
          this.form_dtxSolution_disabled = false;
        } else {
          this.form_dtxSolution_disabled = true;
        }
      },
    }
  };
</script>
